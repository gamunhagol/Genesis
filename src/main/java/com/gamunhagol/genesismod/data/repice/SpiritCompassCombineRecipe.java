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
            "plants", "minecraft:mansion",
            "ice", "minecraft:igloo"
    );

    public SpiritCompassCombineRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int compassCount = 0;
        int stoneCount = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(GenesisItems.SPIRIT_COMPASS.get())) {
                if (stack.getOrCreateTag().getBoolean(SpiritCompassItem.KEY_HAS_NEEDLE))
                    return false;
                compassCount++;
            } else if (stack.is(ModItemTagGenerator.SPIRIT_STONES)) {
                stoneCount++;
            } else return false;
        }
        return compassCount == 1 && stoneCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack compass = ItemStack.EMPTY;
        ItemStack stone = ItemStack.EMPTY;
        int compassSlot = -1;
        int stoneSlot = -1;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (s.is(GenesisItems.SPIRIT_COMPASS.get())) {
                compass = s.copy();
                compassSlot = i;
            } else if (s.is(ModItemTagGenerator.SPIRIT_STONES)) {
                stone = s.copy();
                stoneSlot = i;
            }
        }

        if (compass.isEmpty() || stone.isEmpty()) return ItemStack.EMPTY;

        String color = STONE_COLOR_MAP.getOrDefault(stone.getItem().builtInRegistryHolder().key().location(), "blue");
        String structureKey = COLOR_TO_STRUCTURE.getOrDefault(color, "minecraft:ancient_city");

        ItemStack result = compass.copy();
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, color);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_TARGET, structureKey);

        // ⚙️ 조합 후 입력 아이템 직접 소모
        if (compassSlot >= 0) {
            ItemStack slotStack = inv.getItem(compassSlot);
            slotStack.shrink(1); // 나침반 1개 소비
        }
        if (stoneSlot >= 0) {
            ItemStack slotStack = inv.getItem(stoneSlot);
            slotStack.shrink(1); // 침 재료 1개 소비
        }

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.isEmpty()) continue;

            // 나침반, 정령석은 전부 소비됨
            if (stack.is(GenesisItems.SPIRIT_COMPASS.get()) || stack.is(ModItemTagGenerator.SPIRIT_STONES)) {
                remains.set(i, ItemStack.EMPTY);
            }
            // 버킷류 등 craftingRemainingItem이 있는 경우
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
        return com.gamunhagol.genesismod.data.repice.ModRecipeSerializers.SPIRIT_COMPASS_COMBINE.get();
    }
}

