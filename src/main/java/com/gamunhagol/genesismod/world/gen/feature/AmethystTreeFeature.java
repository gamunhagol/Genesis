package com.gamunhagol.genesismod.world.gen.feature;


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
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Optional;

public class AmethystTreeFeature extends Feature<NoneFeatureConfiguration> {
    public AmethystTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        // 지표면인지 확인 (공기 블록이고 바닥이 고체여야 함)
        if (!level.isEmptyBlock(pos) || level.isEmptyBlock(pos.below())) {
            return false;
        }

        StructureTemplateManager manager = level.getLevel().getServer().getStructureManager();
        int treeNum = random.nextInt(5) + 1;
        ResourceLocation location = new ResourceLocation("genesis", "amethyst_tree_" + treeNum);
        Optional<StructureTemplate> template = manager.get(location);

        if (template.isEmpty()) return false;

        StructureTemplate t = template.get();
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.getRandom(random))
                .setMirror(Mirror.NONE)
                .addProcessor(new RuleProcessor(ImmutableList.of(
                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.AMETHYST_BLOCK, 0.2f),
                                AlwaysTrueTest.INSTANCE,
                                Blocks.BUDDING_AMETHYST.defaultBlockState()
                        )
                )));

        Vec3i size = t.getSize();
        // 앞서 설정한 -4, 1, -4 오프셋을 고려한 중앙 정렬 로직
        BlockPos pivotPos = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);
        BlockPos originPos = pos.subtract(StructureTemplate.calculateRelativePosition(settings, pivotPos));

        return t.placeInWorld(level, originPos, originPos, settings, random, 2);
    }
}