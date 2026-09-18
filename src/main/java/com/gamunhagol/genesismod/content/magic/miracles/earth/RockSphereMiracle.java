package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class RockSphereMiracle extends MiracleSpell {
    public RockSphereMiracle() { super("rock_sphere"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 15);
    }

    @Override
    public float getMentalCost() { return 7.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    public MiracleElement getElement() { return MiracleElement.EARTH; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        BlockPos center = caster.blockPosition().above(1);
        int radius = 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (Math.round(dist) == radius) {
                        BlockPos targetPos = center.offset(x, y, z);

                        if (level.getBlockState(targetPos).canBeReplaced()) {
                            level.setBlock(targetPos, Blocks.STONE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        level.playSound(null, center, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}