package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.world.structure.SpiritStructureFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


import javax.annotation.Nullable;
import java.util.List;

public class SpiritCompassItem extends CompassItem {
    public static final String KEY_HAS_NEEDLE = "HasNeedle";
    public static final String KEY_NEEDLE_TYPE = "NeedleType";
    public static final String KEY_TARGET = "TargetStructure";

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
        // 0.0~1.0 범위 RGB
        return switch (type) {
            case "fire"      -> new Vector3f(1.00f, 0.35f, 0.35f);
            case "water"     -> new Vector3f(0.38f, 0.56f, 1.00f);
            case "earth"     -> new Vector3f(1.00f, 0.74f, 0.36f);
            case "storm"     -> new Vector3f(0.60f, 0.76f, 0.82f);
            case "lightning" -> new Vector3f(0.95f, 1.00f, 0.49f);
            case "plants"    -> new Vector3f(0.49f, 1.00f, 0.53f);
            case "ice"       -> new Vector3f(0.54f, 0.85f, 0.95f);
            default          -> new Vector3f(1.00f, 1.00f, 1.00f);
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 🔸 쿨다운 중이면 무시
        if (player.getCooldowns().isOnCooldown(this)) {
            if (level.isClientSide) {
                player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 0.5f, 1.6f);
            }
            return InteractionResultHolder.fail(stack);
        }

        // 🔹 서버 측 처리
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            CompoundTag tag = stack.getOrCreateTag();
            String targetStr = tag.getString(KEY_TARGET);
            if (targetStr == null || targetStr.isEmpty()) {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.no_target").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            BlockPos target = SpiritStructureFinder.findNearest(serverLevel, targetStr, player.blockPosition(), 1200);

            if (target == null) {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.not_found").withStyle(ChatFormatting.GRAY), true);
                return InteractionResultHolder.fail(stack);
            }

            // 🔹 사운드 재생 (한 번만)
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

            // 🔹 방향 계산 및 파티클 생성 (중복 제거)
            double dx = target.getX() + 0.5 - player.getX();
            double dy = (target.getY() + 1.5) - player.getEyeY();
            double dz = target.getZ() + 0.5 - player.getZ();
            double len = Math.max(Math.sqrt(dx*dx + dy*dy + dz*dz), 0.0001);
            dx /= len; dy /= len; dz /= len;

            String needle = tag.getString(KEY_NEEDLE_TYPE);
            DustParticleOptions dust = new DustParticleOptions(colorFor(needle), 1.2f);

            // 7블록 정도만 색상 파티클로 표시
            for (int i = 1; i <= 14; i++) {
                double t = i * 0.5;
                serverLevel.sendParticles(dust, player.getX() + dx * t, player.getEyeY() + dy * t, player.getZ() + dz * t, 4, 0.02, 0.02, 0.02, 0.0);
            }


            // ⏳ 쿨다운 5초
            player.getCooldowns().addCooldown(this, 100); // 100 tick = 5초

        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

}
