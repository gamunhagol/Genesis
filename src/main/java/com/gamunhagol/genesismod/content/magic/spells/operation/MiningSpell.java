package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class MiningSpell extends MagicSpell {
    public MiningSpell() { super("mining"); }

    @Override
    public int getCastTime() { return 20; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 10);
    }

    @Override
    public float getMentalCost() { return 1.5f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double reach = 5.0D;
        HitResult hitResult = caster.pick(reach, 1.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (state.getDestroySpeed(level, pos) < 0) return;

            CompoundTag data = caster.getPersistentData();
            long storedPos = data.getLong("GenesisMiningPos");
            int progress = data.getInt("GenesisMiningProgress");

            if (storedPos == pos.asLong()) {
                progress++;
            } else {
                progress = 1;
                data.putLong("GenesisMiningPos", pos.asLong());
            }

            int fakeEntityId = caster.getId() + 10000;

            if (progress >= 3) {
                level.destroyBlock(pos, false, caster);
                data.remove("GenesisMiningPos");
                data.remove("GenesisMiningProgress");

                level.destroyBlockProgress(fakeEntityId, pos, -1);
            } else {
                data.putInt("GenesisMiningProgress", progress);

                int crackStage = progress * 3;
                level.destroyBlockProgress(fakeEntityId, pos, crackStage);

                level.playSound(null, pos, state.getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}