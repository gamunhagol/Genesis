package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class PushBlockSpell extends MagicSpell {
    public PushBlockSpell() { super("push_block"); }

    @Override
    public int getCastTime() { return 15; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() { return 4.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        CompoundTag data = caster.getPersistentData();

        if (data.hasUUID("GenesisGrabbedEntity")) {
            UUID uuid = data.getUUID("GenesisGrabbedEntity");
            Entity grabbed = serverLevel.getEntity(uuid);
            data.remove("GenesisGrabbedEntity");

            if (grabbed instanceof FallingBlockEntity fallingBlock) {
                fallingBlock.setNoGravity(false);
                fallingBlock.setHurtsEntities(3.0F, 20);

                Vec3 look = caster.getLookAngle();
                fallingBlock.setDeltaMovement(look.scale(1.5D));
                fallingBlock.hasImpulse = true;

                level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        else {
            HitResult hitResult = caster.pick(5.0D, 1.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                BlockState state = level.getBlockState(pos);

                if (isValidTarget(state, level, pos)) {

                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);

                    Vec3 look = caster.getLookAngle();
                    Vec3 spawnPos = caster.getEyePosition().add(look.scale(2.0D));
                    fallingBlock.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

                    fallingBlock.setNoGravity(true);
                    fallingBlock.time = 1;

                    data.putUUID("GenesisGrabbedEntity", fallingBlock.getUUID());

                    level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    private boolean isValidTarget(BlockState state, Level level, BlockPos pos) {
        if (state.isAir()) return false;
        if (!state.getFluidState().isEmpty()) return false;
        if (state.hasBlockEntity()) return false;
        if (state.getDestroySpeed(level, pos) < 0) return false;

        Block b = state.getBlock();
        if (b == Blocks.NETHER_PORTAL || b == Blocks.END_PORTAL || b == Blocks.END_PORTAL_FRAME || b == Blocks.END_GATEWAY) {
            return false;
        }
        return true;
    }
}