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

            BlockPos playerPos = player.blockPosition();
            BlockPos target = SpiritStructureFinder.findNearest(serverLevel, targetStr, playerPos, 6400);

            if (target == null) {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.not_found").withStyle(ChatFormatting.GRAY), true);
                return InteractionResultHolder.fail(stack);
            }

            // 🔹 목표 구조물 방향 벡터 계산
            Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
            Vec3 direction = new Vec3(
                    target.getX() - start.x,
                    target.getY() - start.y,
                    target.getZ() - start.z
            ).normalize();

            // 🔹 파티클 직선 뿌리기 (7블록 정도)
            for (int i = 1; i <= 7; i++) {
                double px = start.x + direction.x * i;
                double py = start.y + direction.y * i;
                double pz = start.z + direction.z * i;

                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        px, py, pz,
                        4, 0.1, 0.1, 0.1, 0.01);
            }


            serverLevel.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, // ✅ 군집(Cluster) 깨지는 소리
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );

            // ⏳ 쿨다운 5초
            player.getCooldowns().addCooldown(this, 100); // 100 tick = 5초

            // ■ 사운드: '자수정 군집' 깨지는 소리
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

// ■ 목표 방향 벡터(+ 정규화)
            double dx = target.getX() + 0.5 - player.getX();
            double dy = (target.getY() + 1.5) - (player.getY() + player.getEyeHeight());
            double dz = target.getZ() + 0.5 - player.getZ();
            double len = Math.sqrt(dx*dx + dy*dy + dz*dz);
            if (len < 0.0001) len = 0.0001;
            dx /= len; dy /= len; dz /= len;

// ■ 7블록 직선으로 트레이스
            int steps = 14;              // 7블록 / 0.5 간격
            double step = 0.5;

// 색상: 정령 타입별 Dust(레드스톤) + END_ROD 섞어 뿌림
            String needle = stack.getOrCreateTag().getString(KEY_NEEDLE_TYPE);
            Vector3f rgb = colorFor(needle);
            DustParticleOptions dust = new DustParticleOptions(rgb, 1.2f); // size 1.2

            for (int i = 1; i <= steps; i++) {
                double t = i * step;
                double px = player.getX() + dx * t;
                double py = player.getEyeY() + dy * t;
                double pz = player.getZ() + dz * t;

                // 컬러 파편
                serverLevel.sendParticles(dust, px, py, pz, 6, 0.02, 0.02, 0.02, 0.0);

                // 빛 기둥 느낌 (눈에 잘 띄게)
                serverLevel.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }

        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

}
