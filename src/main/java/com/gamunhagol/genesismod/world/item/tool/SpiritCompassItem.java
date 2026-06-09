package com.gamunhagol.genesismod.world.item.tool;

import com.gamunhagol.genesismod.world.structure.SpiritBlockFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

public class SpiritCompassItem extends CompassItem {
    public static final String KEY_HAS_NEEDLE = "HasNeedle";
    public static final String KEY_NEEDLE_TYPE = "NeedleType";
    public static final String KEY_TARGET = "TargetBlock";

    private static final Vector3f COLOR_FIRE      = new Vector3f(1.00f, 0.35f, 0.35f);
    private static final Vector3f COLOR_WATER     = new Vector3f(0.38f, 0.56f, 1.00f);
    private static final Vector3f COLOR_EARTH     = new Vector3f(1.00f, 0.74f, 0.36f);
    private static final Vector3f COLOR_STORM     = new Vector3f(0.60f, 0.76f, 0.82f);
    private static final Vector3f COLOR_LIGHTNING = new Vector3f(0.95f, 1.00f, 0.49f);
    private static final Vector3f COLOR_PLANTS    = new Vector3f(0.49f, 1.00f, 0.53f);
    private static final Vector3f COLOR_ICE       = new Vector3f(0.54f, 0.85f, 0.95f);
    private static final Vector3f COLOR_DEFAULT   = new Vector3f(1.00f, 1.00f, 1.00f);

    public SpiritCompassItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean has = tag.getBoolean(KEY_HAS_NEEDLE);

        tip.add(Component.translatable(
                        has ? "tooltip.genesis.spirit_compass.has_needle" : "tooltip.genesis.spirit_compass.no_needle")
                .withStyle(has ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));

        if (has) {
            String type = tag.getString(KEY_NEEDLE_TYPE);
            tip.add(Component.translatable(
                            "tooltip.genesis.spirit_compass.type",
                            Component.translatable("tooltip.genesis.spirit_type." + type))
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    @Nullable
    public static GlobalPos getCompassTarget(ItemStack stack, @Nullable ClientLevel level) {
        if (!stack.hasTag() || !stack.getTag().contains("LodestonePos")) return null;

        CompoundTag tag = stack.getTag().getCompound("LodestonePos");
        String dim = stack.getTag().getString("LodestoneDimension");
        ResourceLocation dimLoc = ResourceLocation.tryParse(dim);
        if (dimLoc == null) return null;

        ResourceKey<Level> worldKey = ResourceKey.create(Registries.DIMENSION, dimLoc);
        BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        return GlobalPos.of(worldKey, pos);
    }

    @Override
    public boolean isFoil(ItemStack stack) { return false; }

    @Override
    public Component getName(ItemStack stack) { return Component.translatable(this.getDescriptionId(stack)); }

    @Override
    public String getDescriptionId(ItemStack stack) { return "item.genesis.spirit_compass"; }

    private static Vector3f colorFor(String type) {
        return switch (type) {
            case "fire"      -> COLOR_FIRE;
            case "water"     -> COLOR_WATER;
            case "earth"     -> COLOR_EARTH;
            case "storm"     -> COLOR_STORM;
            case "lightning" -> COLOR_LIGHTNING;
            case "plants"    -> COLOR_PLANTS;
            case "ice"       -> COLOR_ICE;
            default          -> COLOR_DEFAULT;
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            if (level.isClientSide) {
                player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 0.5f, 1.6f);
            }
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            CompoundTag tag = stack.getOrCreateTag();
            String targetStr = tag.getString(KEY_TARGET);
            String needleType = tag.getString(KEY_NEEDLE_TYPE);

            if (targetStr == null || targetStr.isEmpty()) {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.no_target").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            player.getCooldowns().addCooldown(this, 60);

            BlockPos playerPos = player.blockPosition();
            List<String> targetList = java.util.Arrays.asList(targetStr.split(","));

            List<BlockPos> targetPositions = SpiritBlockFinder.findNearestBlocksByChunk(serverLevel, targetList, playerPos, 7, -64, 320, 7);

            if (!targetPositions.isEmpty()) {
                this.spawnCompassParticles(serverLevel, player, targetPositions, needleType);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.not_found").withStyle(ChatFormatting.GRAY), true);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private void spawnCompassParticles(ServerLevel serverLevel, Player player, List<BlockPos> targets, String needle) {
        net.minecraft.core.particles.DustParticleOptions dust = new net.minecraft.core.particles.DustParticleOptions(colorFor(needle), 1.2f);

        for (BlockPos target : targets) {
            double dx = target.getX() + 0.5 - player.getX();
            double dy = target.getY() + 0.5 - player.getEyeY();
            double dz = target.getZ() + 0.5 - player.getZ();

            double distance = Math.max(Math.sqrt(dx*dx + dy*dy + dz*dz), 0.0001);

            double spread = 0.25;
            double dirX = (dx / distance) + (serverLevel.random.nextDouble() - 0.5) * spread;
            double dirY = (dy / distance) + (serverLevel.random.nextDouble() - 0.5) * spread;
            double dirZ = (dz / distance) + (serverLevel.random.nextDouble() - 0.5) * spread;

            double newDist = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
            dirX /= newDist;
            dirY /= newDist;
            dirZ /= newDist;

            int particleCount = 5;
            for (int i = 1; i <= particleCount; i++) {
                double t = i * 0.45;
                serverLevel.sendParticles(dust,
                        player.getX() + dirX * t,
                        player.getEyeY() + dirY * t,
                        player.getZ() + dirZ * t,
                        1, 0.02, 0.02, 0.02, 0.0);
            }

        }
    }
}