package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.client.renderer.model.item.SpiritCompassISTER;
import com.gamunhagol.genesismod.world.structure.SpiritStructureFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SpiritCompassItem extends CompassItem {
    public static final String KEY_HAS_NEEDLE = "HasNeedle";
    public static final String KEY_NEEDLE_TYPE = "NeedleType";
    public static final String KEY_TARGET = "TargetStructure";

    public SpiritCompassItem(Properties props) {
        super(props);
    }

    // ─────────────────────────────────────────────────────────────
    // Tooltip 표시
    // ─────────────────────────────────────────────────────────────
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean has = tag.getBoolean(KEY_HAS_NEEDLE);

        tip.add(Component.translatable(
                        has ? "tooltip.genesis.spirit_compass.has_needle" : "tooltip.genesis.spirit_compass.no_needle")
                .withStyle(has ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY)
        );

        if (has) {
            String type = tag.getString(KEY_NEEDLE_TYPE);
            tip.add(Component.translatable(
                    "tooltip.genesis.spirit_compass.type",
                    Component.translatable("tooltip.genesis.spirit_type." + type)
            ).withStyle(ChatFormatting.YELLOW));
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Lodestone 목표 좌표
    // ─────────────────────────────────────────────────────────────
    @Nullable
    public static GlobalPos getCompassTarget(ItemStack stack, @Nullable ClientLevel level) {
        if (!stack.hasTag() || !stack.getTag().contains("LodestonePos")) return null;

        CompoundTag tag = stack.getTag().getCompound("LodestonePos");
        String dim = stack.getTag().getString("LodestoneDimension");
        ResourceLocation dimLoc = ResourceLocation.tryParse(dim);
        if (dimLoc == null) return null;

        ResourceKey<Level> worldKey = ResourceKey.create(Registries.DIMENSION, dimLoc);
        BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        if (level != null && !level.dimension().location().equals(dimLoc)) {
            return GlobalPos.of(level.dimension(), pos);
        }
        return GlobalPos.of(worldKey, pos);
    }

    // ─────────────────────────────────────────────────────────────
    // 나침반 각도 계산 (ISTER와 모델 프리디케이트에서 공용)
    // ─────────────────────────────────────────────────────────────
    public static float calculateCompassAngle(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (level == null || entity == null) return 0.0F;
        GlobalPos target = getCompassTarget(stack, level);
        if (target == null) return 0.0F;

        double dx = target.pos().getX() + 0.5 - entity.getX();
        double dz = target.pos().getZ() + 0.5 - entity.getZ();
        double angleRad = Math.atan2(dz, dx);
        float degrees = (float) Math.toDegrees(angleRad) - entity.getYRot();
        return (degrees % 360 + 360) % 360;
    }

    // ─────────────────────────────────────────────────────────────
    // Epic Fight 호환용 렌더러 연결
    // ─────────────────────────────────────────────────────────────
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final SpiritCompassISTER renderer = new SpiritCompassISTER();

            @Override
            public SpiritCompassISTER getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public boolean isFoil(ItemStack stack) { return false; }
    @Override
    public Component getName(ItemStack stack) { return Component.translatable(this.getDescriptionId(stack)); }
    @Override
    public String getDescriptionId(ItemStack stack) { return "item.genesis.spirit_compass"; }
}
