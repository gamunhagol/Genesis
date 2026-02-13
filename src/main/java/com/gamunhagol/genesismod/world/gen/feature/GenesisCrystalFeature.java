package com.gamunhagol.genesismod.world.gen.feature;

import com.gamunhagol.genesismod.world.gen.feature.configurations.GenesisCrystalConfiguration;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Optional;

public class GenesisCrystalFeature extends Feature<GenesisCrystalConfiguration> {
    public GenesisCrystalFeature(Codec<GenesisCrystalConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GenesisCrystalConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        GenesisCrystalConfiguration config = context.config();

        // 1. 배치 가능 여부 확인
        // WorldGenLevel에는 isInWorldBounds가 없으므로 isOutsideBuildHeight를 사용합니다.
        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }

        StructureTemplateManager manager = level.getLevel().getServer().getStructureManager();

        // 2. NBT 파일 선택 (경로 + "_" + 랜덤번호)
        // 예: genesis:citrine_geode_1 또는 genesis:citrine_geode_2
        int variantNum = random.nextInt(config.variants()) + 1;
        ResourceLocation location = new ResourceLocation(config.structurePath().getNamespace(),
                config.structurePath().getPath() + "_" + variantNum);

        Optional<StructureTemplate> template = manager.get(location);
        if (template.isEmpty()) return false;

        StructureTemplate t = template.get();
        Vec3i size = t.getSize();

        // 3. 프로세서 설정 (공기 치환 로직 수정)
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.getRandom(random))
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(true)
                .addProcessor(new RuleProcessor(ImmutableList.of(
                        new ProcessorRule(
                                // [수정 1] 첫 번째 인자: NBT 블록이 'targetBlock'이면서 + '확률'에 당첨되었는지 검사
                                new RandomBlockMatchTest(config.targetBlock().getBlock(), config.airChance()),
                                // [수정 2] 두 번째 인자: 월드에 원래 있던 블록이 무엇이든 상관없음 (무조건 통과)
                                AlwaysTrueTest.INSTANCE,
                                // [결과] 위 두 조건이 맞으면 공기로 바꿈
                                Blocks.AIR.defaultBlockState()
                        )
                )));

        // 4. 위치 조정 (중앙 정렬 + Y 오프셋 적용)
        // 구조물의 중앙을 기준으로 회전시키고, config에 설정된 만큼 높이를 조절합니다.
        BlockPos pivotPos = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);
        BlockPos centerPos = pos.subtract(StructureTemplate.calculateRelativePosition(settings, pivotPos));
        BlockPos finalPos = centerPos.offset(0, config.yOffset(), 0);

        // 5. 월드에 배치 (flags: 2 = 클라이언트 업데이트)
        return t.placeInWorld(level, finalPos, finalPos, settings, random, 2);
    }
}