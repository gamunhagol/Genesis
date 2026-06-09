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

        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }

        StructureTemplateManager manager = level.getLevel().getServer().getStructureManager();

        int variantNum = random.nextInt(config.variants()) + 1;
        ResourceLocation location = new ResourceLocation(config.structurePath().getNamespace(),
                config.structurePath().getPath() + "_" + variantNum);

        Optional<StructureTemplate> template = manager.get(location);
        if (template.isEmpty()) return false;

        StructureTemplate t = template.get();
        Vec3i size = t.getSize();

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.getRandom(random))
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(true)
                .addProcessor(new RuleProcessor(ImmutableList.of(
                        new ProcessorRule(
                                new RandomBlockMatchTest(config.targetBlock().getBlock(), config.airChance()),
                                AlwaysTrueTest.INSTANCE,
                                Blocks.AIR.defaultBlockState()
                        )
                )));
        BlockPos pivotPos = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);
        BlockPos centerPos = pos.subtract(StructureTemplate.calculateRelativePosition(settings, pivotPos));
        BlockPos finalPos = centerPos.offset(0, config.yOffset(), 0);
        return t.placeInWorld(level, finalPos, finalPos, settings, random, 2);
    }
}