package com.gamunhagol.genesismod.data.repice;

import com.gamunhagol.genesismod.datagen.ModItemTagGenerator;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SpiritCompassCombineRecipe extends CustomRecipe {

    private static final Map<ResourceLocation, String> STONE_COLOR_MAP = Map.of(
            GenesisItems.BLUE_CRYSTAL_SHARD.getId(), "water",
            GenesisItems.RED_CRYSTAL_SHARD.getId(), "fire",
            GenesisItems.CITRINE_SHARD.getId(), "earth",
            GenesisItems.WIND_STONE.getId(), "storm",
            GenesisItems.LIGHTING_CRYSTAL_SHARD.getId(), "lightning",
            GenesisItems.GREEN_AMBER.getId(), "plants",
            GenesisItems.FROST_CRYSTAL_SHARD.getId(), "ice"
    );

    private static final Map<String, String> COLOR_TO_STRUCTURE = Map.of(
            "water", "minecraft:ocean_monument",
            "fire", "minecraft:fortress",
            "earth", "minecraft:ancient_city",
            "storm", "minecraft:end_city",
            "lightning", "minecraft:stronghold",
            "plants", "minecraft:jungle_temple",
            "ice", "minecraft:igloo"
    );

    public SpiritCompassCombineRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    // ✅ 조합 조건: 나침반 1개 + 정령석 1개
    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int compassCount = 0;
        int stoneCount = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(GenesisItems.SPIRIT_COMPASS.get())) {
                if (stack.getOrCreateTag().getBoolean(SpiritCompassItem.KEY_HAS_NEEDLE))
                    return false; // 이미 침이 있으면 불가능
                compassCount++;
            } else if (stack.is(ModItemTagGenerator.SPIRIT_STONES)) {
                stoneCount++;
            } else {
                return false; // 다른 아이템 포함 시 조합 불가
            }
        }

        return compassCount == 1 && stoneCount == 1;
    }

    // ✅ 결과 생성: 침 속성 추가
    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack compass = ItemStack.EMPTY;
        ItemStack stone = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (s.is(GenesisItems.SPIRIT_COMPASS.get())) {
                compass = s.copy();
            } else if (s.is(ModItemTagGenerator.SPIRIT_STONES)) {
                stone = s.copy();
            }
        }

        if (compass.isEmpty() || stone.isEmpty()) return ItemStack.EMPTY;

        String color = STONE_COLOR_MAP.getOrDefault(stone.getItem().builtInRegistryHolder().key().location(), "blue");
        String structureKey = COLOR_TO_STRUCTURE.getOrDefault(color, "minecraft:ancient_city");

        ItemStack result = compass.copy();
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, color);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_TARGET, structureKey);

        return result;
    }

    // ✅ 남는 아이템 처리 — 조합 재료 소모 확정
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            // 나침반과 정령석은 소모됨
            if (stack.is(GenesisItems.SPIRIT_COMPASS.get()) || stack.is(ModItemTagGenerator.SPIRIT_STONES)) {
                remains.set(i, ItemStack.EMPTY);
            }
            // 버킷류 등 조합 후 남는 아이템은 반환
            else if (stack.getItem().hasCraftingRemainingItem()) {
                remains.set(i, new ItemStack(stack.getItem().getCraftingRemainingItem()));
            }
        }

        return remains;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SPIRIT_COMPASS_COMBINE.get();
    }
}


