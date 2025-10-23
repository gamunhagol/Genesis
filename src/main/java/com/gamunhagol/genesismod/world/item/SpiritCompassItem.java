package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.world.structure.SpiritStructureFinder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * Spirit Compass — 지정된 구조물 방향을 추적하는 커스텀 나침반
 */
public class SpiritCompassItem extends CompassItem {
    public static final String KEY_HAS_NEEDLE = "HasNeedle";
    public static final String KEY_NEEDLE_TYPE = "NeedleType";
    public static final String KEY_TARGET = "TargetStructure";

    public SpiritCompassItem(Properties props) {
        super(props);
    }

    /** Tooltip 표시 (침 유무 / 속성명) */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean has = tag.getBoolean(KEY_HAS_NEEDLE);

        tip.add(Component.translatable(
                        has ? "tooltip.genesis.spirit_compass.has_needle" : "tooltip.genesis.spirit_compass.no_needle")
                .withStyle(has ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));

        if (has) {
            String type = tag.getString(KEY_NEEDLE_TYPE);
            tip.add(Component.translatable("tooltip.genesis.spirit_compass.type",
                            Component.translatable("tooltip.genesis.spirit_type." + type))
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    /** 제작 시 자동으로 구조물 좌표 저장 */
    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.getBoolean(KEY_HAS_NEEDLE)) return;
        String structureKey = tag.getString(KEY_TARGET);
        if (structureKey.isEmpty()) return;

        if (!level.isClientSide() && level instanceof ServerLevel server) {
            BlockPos origin = player.blockPosition();
            BlockPos pos = SpiritStructureFinder.findNearest(server, structureKey, origin, 6400);

            if (pos != null) {
                CompoundTag lodestoneTag = new CompoundTag();
                lodestoneTag.putInt("x", pos.getX());
                lodestoneTag.putInt("y", pos.getY());
                lodestoneTag.putInt("z", pos.getZ());
                tag.put("LodestonePos", lodestoneTag);
                tag.putString("LodestoneDimension", server.dimension().location().toString());
                tag.putBoolean("LodestoneTracked", true);

                stack.setTag(tag);
                player.getInventory().setChanged();
                player.containerMenu.broadcastChanges();
            }
        }
    }

    /** 서버에서 주기적으로 구조물 좌표를 다시 찾음 */
    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.getBoolean(KEY_HAS_NEEDLE) || !tag.contains(KEY_TARGET)) return;

        String target = tag.getString(KEY_TARGET);
        BlockPos origin = entity.blockPosition();

        if (level.getGameTime() % 200 == 0 && level instanceof ServerLevel server) {
            BlockPos pos = SpiritStructureFinder.findNearest(server, target, origin, 6400);
            if (pos != null) {
                CompoundTag lodestoneTag = new CompoundTag();
                lodestoneTag.putInt("x", pos.getX());
                lodestoneTag.putInt("y", pos.getY());
                lodestoneTag.putInt("z", pos.getZ());
                tag.put("LodestonePos", lodestoneTag);
                tag.putString("LodestoneDimension", server.dimension().location().toString());
                tag.putBoolean("LodestoneTracked", true);
                stack.setTag(tag);
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) { return false; }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack));
    }
    public boolean isCompass(ItemStack stack) {
        return true;
    }
}