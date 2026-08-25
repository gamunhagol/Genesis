package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlessingEvents {


    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                if (cap.isNodeUnlocked("god_a", 1) && player.isCrouching()) {
                    event.setDistance(event.getDistance() * 0.3F);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                if (cap.isNodeUnlocked("god_e", 1)) {
                    event.modifyVisibility(0.65D);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {

                if (stats.isNodeUnlocked("god_g", 1)) {
                    Vec3 delta = player.getDeltaMovement();
                    boolean isStationary = delta.horizontalDistanceSqr() < 0.001D && player.onGround() && !player.isUsingItem();

                    if (isStationary) {
                        int currentTicks = stats.getIceIdleTicks();
                        if (currentTicks < 200) {
                            stats.setIceIdleTicks(currentTicks + 1);
                        }

                        if (stats.getIceIdleTicks() >= 200) {
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 25, 1, false, false, true));
                        }
                    } else {
                        stats.setIceIdleTicks(0);
                    }
                }


                if (stats.isNodeUnlocked("god_f", 1)) {
                    if (player.isCrouching()) {
                        stats.setForestSneakTicks(stats.getForestSneakTicks() + 1);
                        if (stats.getForestSneakTicks() >= 200) {
                            applyBonemealEffect(player);
                            stats.setForestSneakTicks(0);
                        }
                    } else {
                        stats.setForestSneakTicks(0);
                    }
                }
            });
        }
    }

    private static void applyBonemealEffect(Player player) {
        Level level = player.level();
        BlockPos center = player.blockPosition();
        int radius = 3;

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -1, -radius), center.offset(radius, 1, radius))) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof BonemealableBlock bonemealable) {
                if (bonemealable.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                    if (level instanceof ServerLevel serverLevel) {
                        if (bonemealable.isBonemealSuccess(level, level.getRandom(), pos, state)) {
                            bonemealable.performBonemeal(serverLevel, level.getRandom(), pos, state);
                            level.levelEvent(2005, pos, 0);
                        }
                    }
                }
            }
        }
    }
}