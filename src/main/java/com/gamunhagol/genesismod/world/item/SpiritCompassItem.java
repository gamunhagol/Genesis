package com.gamunhagol.genesismod.world.item;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Spirit Compass — 제작 시 구조물을 자동 지정하고 그 방향을 추적하는 나침반
 * - Lodestone 기반 CompassItem 확장
 * - 반짝임 제거, 자석석 이름 방지
 * - 구조물 위치 주기적 갱신 및 클라이언트 표시
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
                .withStyle(has ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY)
        );

        if (has) {
            String type = tag.getString(KEY_NEEDLE_TYPE);
            tip.add(Component.translatable(
                            "tooltip.genesis.spirit_compass.type",
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

        // 10초(200틱)마다 위치 갱신
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
            }
        }
    }

    /** 반짝임 제거 */
    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    /** 이름 고정 ("정령 나침반") */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack));
    }

    /** 📍 클라이언트용 Lodestone 위치 반환 (렌더러에서 사용됨) */
    @Nullable
    public static GlobalPos getCompassTarget(ItemStack stack, @Nullable ClientLevel level) {
        if (stack.hasTag() && stack.getTag().contains("LodestonePos")) {
            var tag = stack.getTag().getCompound("LodestonePos");
            String dim = stack.getTag().getString("LodestoneDimension");

            var dimLoc = ResourceLocation.tryParse(dim);
            if (dimLoc == null) return null;

            var worldKey = ResourceKey.create(Registries.DIMENSION, dimLoc);
            var pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
            return GlobalPos.of(worldKey, pos);
        }
        return null;
    }
    // Lodestone 전용 이름 변환 방지
    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.genesis.spirit_compass";
    }

}