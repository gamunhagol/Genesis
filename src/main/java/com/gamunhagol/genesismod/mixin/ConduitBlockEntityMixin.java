package com.gamunhagol.genesismod.mixin;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public abstract class ConduitBlockEntityMixin {

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void genesis$onServerTick(Level level, BlockPos pos, BlockState state, ConduitBlockEntity blockEntity, CallbackInfo ci) {
        if (!blockEntity.isActive()) {
            return;
        }

        long gameTime = level.getGameTime();
        if (gameTime % 20L != 0L) {
            return;
        }

        int eyeCount = genesis$countEyesOfTheSea(level, pos);
        if (eyeCount <= 0) {
            return;
        }

        double multiplier = 1.0 + (eyeCount * 0.1);

        if (gameTime % 40L == 0L) {
            genesis$applyExtendedEffects(level, pos, blockEntity, multiplier);
        }

        if (blockEntity.isHunting()) {
            if (gameTime % 40L == 0L || eyeCount >= 10) {
                genesis$applyExtendedAttack(level, pos, multiplier, gameTime % 40L != 0L);
            }
        }
    }

    @Unique
    private static int genesis$countEyesOfTheSea(Level level, BlockPos center) {
        int count = 0;
        int radius = 12;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z > radius * radius) {
                        continue;
                    }
                    mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockState targetState = level.getBlockState(mutablePos);
                    if (targetState.is(GenesisBlocks.EYE_OF_THE_SEA.get()) && genesis$isSubmerged(level, mutablePos)) {
                        count++;
                        if (count >= 10) {
                            return 10;
                        }
                    }
                }
            }
        }
        return count;
    }

    @Unique
    private static boolean genesis$isSubmerged(Level level, BlockPos pos) {
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            return true;
        }
        return level.getFluidState(pos.above()).is(FluidTags.WATER)
                || level.getFluidState(pos.below()).is(FluidTags.WATER)
                || level.getFluidState(pos.north()).is(FluidTags.WATER)
                || level.getFluidState(pos.south()).is(FluidTags.WATER)
                || level.getFluidState(pos.east()).is(FluidTags.WATER)
                || level.getFluidState(pos.west()).is(FluidTags.WATER);
    }

    @Unique
    private static void genesis$applyExtendedEffects(Level level, BlockPos pos, ConduitBlockEntity blockEntity, double multiplier) {
        int frameCount = genesis$countFrameBlocks(level, pos);
        int baseRange = frameCount / 7 * 16;
        if (baseRange <= 0) {
            return;
        }

        double extendedRange = baseRange * multiplier;
        AABB area = new AABB(pos).inflate(extendedRange);
        List<Player> players = level.getEntitiesOfClass(Player.class, area);

        for (Player player : players) {
            if (pos.closerThan(player.blockPosition(), extendedRange) && player.isInWaterOrRain()) {
                player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
            }
        }
    }

    @Unique
    private static int genesis$countFrameBlocks(Level level, BlockPos pos) {
        int count = 0;
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) == 2 || Math.abs(y) == 2 || Math.abs(z) == 2) {
                        BlockPos checkPos = pos.offset(x, y, z);
                        if (level.getBlockState(checkPos).isConduitFrame(level, checkPos, pos)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    @Unique
    private static void genesis$applyExtendedAttack(Level level, BlockPos pos, double multiplier, boolean isExtraTick) {
        double baseAttackRange = 8.0;
        double extendedAttackRange = baseAttackRange * multiplier;
        AABB area = new AABB(pos).inflate(extendedAttackRange);

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity instanceof Enemy && entity.isInWaterOrRain() && entity.isAlive());

        if (targets.isEmpty()) {
            return;
        }

        LivingEntity target = targets.get(level.random.nextInt(targets.size()));
        if (!isExtraTick && pos.closerThan(target.blockPosition(), baseAttackRange)) {
            return;
        }

        target.hurt(level.damageSources().magic(), 4.0F);
    }
}
