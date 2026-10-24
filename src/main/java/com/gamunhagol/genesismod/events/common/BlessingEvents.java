package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlessingEvents {

    public static final int EARTH_PILLAR_NODE = 10;
    public static final int FIRE_LAVA_NODE = 5;

    private static final List<PillarBuildTask> ACTIVE_PILLARS = new ArrayList<>();

    private static class PillarBuildTask {
        final ServerLevel level;
        final BlockPos centerPos;
        final int targetHeight;
        int currentLayer = 1;

        PillarBuildTask(ServerLevel level, BlockPos centerPos, int targetHeight) {
            this.level = level;
            this.centerPos = centerPos;
            this.targetHeight = targetHeight;
        }

        boolean tick() {
            if (currentLayer > targetHeight) return true;

            int dy = currentLayer;
            boolean hitCeiling = false;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos targetPos = centerPos.offset(dx, dy, dz);

                    if (!level.isInWorldBounds(targetPos)) {
                        hitCeiling = true;
                        break;
                    }

                    BlockState existing = level.getBlockState(targetPos);
                    if (existing.getDestroySpeed(level, targetPos) < 0) {
                        hitCeiling = true;
                        break;
                    }
                }
                if (hitCeiling) break;
            }

            if (hitCeiling) {
                return true;
            }

            double layerTopY = centerPos.getY() + dy + 1.0D;
            AABB liftArea = new AABB(
                    centerPos.getX() - 1.0D, centerPos.getY() + dy - 0.5D, centerPos.getZ() - 1.0D,
                    centerPos.getX() + 2.0D, layerTopY + 1.5D, centerPos.getZ() + 2.0D
            );

            List<Entity> entitiesToLift = level.getEntitiesOfClass(Entity.class, liftArea);
            for (Entity entity : entitiesToLift) {
                entity.teleportTo(entity.getX(), layerTopY, entity.getZ());
                entity.resetFallDistance();
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0.0D, entity.getDeltaMovement().z);
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos targetPos = centerPos.offset(dx, dy, dz);
                    BlockState existing = level.getBlockState(targetPos);

                    if (existing.isAir() || existing.canBeReplaced()) {
                        BlockState mixedStone = (level.random.nextFloat() < 0.6F)
                                ? Blocks.STONE.defaultBlockState()
                                : Blocks.COBBLESTONE.defaultBlockState();

                        level.setBlock(targetPos, mixedStone, 3);
                    }
                }
            }

            if (currentLayer % 5 == 1) {
                level.playSound(null, centerPos.above(currentLayer), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 0.9F);
            }

            currentLayer++;
            return false;
        }
    }

    @SubscribeEvent
    public static void onServerTickPillars(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (!ACTIVE_PILLARS.isEmpty()) {
            ACTIVE_PILLARS.removeIf(PillarBuildTask::tick);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (event.getLevel().isClientSide) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.getMainHandItem().isEmpty()) return;

        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
            if (cap.isNodeUnlocked("god_a", EARTH_PILLAR_NODE)) {
                if (isDirtOrStoneVariant(state)) {
                    int availableStones = countStoneMaterials(player);
                    int minRequired = 256;

                    if (availableStones >= minRequired) {
                        int toConsume = Math.min(availableStones, 576);
                        consumeStoneMaterials(player, toConsume);

                        int height = 50;
                        ACTIVE_PILLARS.add(new PillarBuildTask((ServerLevel) level, pos, height));

                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }
        });
    }

    public static void executeFireBlessing(ServerPlayer player, BlockPos pos) {
        if (!player.getMainHandItem().isEmpty()) return;

        double reach = player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) + 1.5D;
        if (player.getEyePosition().distanceToSqr(Vec3.atCenterOf(pos)) > reach * reach) return;

        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
            if (cap.isNodeUnlocked("god_b", FIRE_LAVA_NODE)) {
                if (state.is(Blocks.COBBLESTONE) || state.is(Blocks.STONE) || state.is(Blocks.OBSIDIAN)) {
                    level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        });
    }

    private static boolean isDirtOrStoneVariant(BlockState state) {
        return state.is(BlockTags.DIRT)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(Tags.Blocks.STONE)
                || state.is(Tags.Blocks.COBBLESTONE);
    }

    private static int countStoneMaterials(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                Block block = Block.byItem(stack.getItem());
                if (block != Blocks.AIR && isDirtOrStoneVariant(block.defaultBlockState())) {
                    count += stack.getCount();
                }
            }
        }
        return count;
    }

    private static void consumeStoneMaterials(Player player, int countToRemove) {
        int remaining = countToRemove;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                Block block = Block.byItem(stack.getItem());
                if (block != Blocks.AIR && isDirtOrStoneVariant(block.defaultBlockState())) {
                    int take = Math.min(remaining, stack.getCount());
                    stack.shrink(take);
                    remaining -= take;
                    if (remaining <= 0) break;
                }
            }
        }
    }

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
