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

        // 1. 쿨다운 체크
        if (player.getCooldowns().isOnCooldown(this)) {
            if (level.isClientSide) {
                player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 0.5f, 1.6f);
            }
            return InteractionResultHolder.fail(stack);
        }

        // 2. 서버 측 로직
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            CompoundTag tag = stack.getOrCreateTag();
            String targetStr = tag.getString(KEY_TARGET);
            String needleType = tag.getString(KEY_NEEDLE_TYPE);

            if (targetStr == null || targetStr.isEmpty()) {
                player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.no_target").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            // 쿨다운 즉시 적용 (중복 클릭 방지)
            player.getCooldowns().addCooldown(this, 100);

            BlockPos playerPos = player.blockPosition();

            //  비동기 탐색: 별도의 스레드에서 구조물을 찾습니다.
            java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                List<String> targetList = java.util.Arrays.asList(targetStr.split(","));

                return SpiritStructureFinder.findNearest(serverLevel, targetList, playerPos, 1200);
            }).thenAccept(target -> {
                // 탐색이 완료되면 다시 메인 스레드에서 결과를 처리합니다.
                serverLevel.getServer().execute(() -> {
                    if (target != null && player.isAlive()) {
                        // 성공 이펙트 실행
                        this.spawnCompassParticles(serverLevel, player, target, needleType);
                        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        player.displayClientMessage(Component.translatable("item.genesis.spirit_compass.not_found").withStyle(ChatFormatting.GRAY), true);
                    }
                });
            });
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    // 파티클 생성 로직을 별도 메서드로 분리 (가독성용)
    private void spawnCompassParticles(ServerLevel serverLevel, Player player, BlockPos target, String needle) {
        double dx = target.getX() + 0.5 - player.getX();
        double dy = (target.getY() + 1.5) - player.getEyeY();
        double dz = target.getZ() + 0.5 - player.getZ();
        double len = Math.max(Math.sqrt(dx*dx + dy*dy + dz*dz), 0.0001);
        dx /= len; dy /= len; dz /= len;

        net.minecraft.core.particles.DustParticleOptions dust = new net.minecraft.core.particles.DustParticleOptions(colorFor(needle), 1.2f);
        for (int i = 1; i <= 14; i++) {
            double t = i * 0.5;
            serverLevel.sendParticles(dust, player.getX() + dx * t, player.getEyeY() + dy * t, player.getZ() + dz * t, 4, 0.02, 0.02, 0.02, 0.0);
        }
    }

}
