package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.client.PacketSyncGrabState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
    public PushBlockSpell() {
        super("push_block");
    }

    @Override
    public int getMaxChargeTicks() {
        return 100;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() {
        return 4.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return caster.getPersistentData().hasUUID("GenesisGrabbedEntity");
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (isChargePhase(caster)) {
            return true;
        }
        return super.canCast(caster);
    }

    // 1타 시전 (블록 집기)
    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel)) return;
        CompoundTag data = caster.getPersistentData();

        if (!data.hasUUID("GenesisGrabbedEntity")) {
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

                    UUID entityUuid = fallingBlock.getUUID();
                    data.putUUID("GenesisGrabbedEntity", entityUuid);

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                    if (caster instanceof ServerPlayer serverPlayer) {
                        GenesisNetwork.sendToPlayer(new PacketSyncGrabState(true, entityUuid), serverPlayer);
                    }

                    level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    // 2타 시전 (차징 발사)
    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        CompoundTag data = caster.getPersistentData();

        if (data.hasUUID("GenesisGrabbedEntity")) {
            UUID uuid = data.getUUID("GenesisGrabbedEntity");
            Entity grabbed = serverLevel.getEntity(uuid);
            data.remove("GenesisGrabbedEntity");

            if (caster instanceof ServerPlayer serverPlayer) {
                GenesisNetwork.sendToPlayer(new PacketSyncGrabState(false, null), serverPlayer);
            }

            if (grabbed instanceof FallingBlockEntity fallingBlock) {
                fallingBlock.setNoGravity(false);
                fallingBlock.setHurtsEntities(3.0F, 20);

                float chargeRatio = Math.min(1.0F, (float) chargeTicks / (float) Math.max(1, getMaxChargeTicks()));
                double minForce = 0.25D;
                double maxForce = 1.95D;
                double throwForce = minForce + (Math.pow(chargeRatio, 1.5) * (maxForce - minForce));

                Vec3 look = caster.getLookAngle();
                fallingBlock.setDeltaMovement(look.scale(throwForce));
                fallingBlock.hasImpulse = true;

                level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    private boolean isValidTarget(BlockState state, Level level, BlockPos pos) {
        if (state.isAir()) return false;
        if (!state.getFluidState().isEmpty()) return false;
        if (state.hasBlockEntity()) return false;
        if (state.getDestroySpeed(level, pos) < 0) return false;

        Block b = state.getBlock();
        return b != Blocks.NETHER_PORTAL && b != Blocks.END_PORTAL && b != Blocks.END_PORTAL_FRAME && b != Blocks.END_GATEWAY;
    }
}