package com.gamunhagol.genesismod.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

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
}
