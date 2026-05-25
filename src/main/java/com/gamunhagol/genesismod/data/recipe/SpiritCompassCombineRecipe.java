package com.gamunhagol.genesismod.data.recipe;

import com.gamunhagol.genesismod.datagen.tags.ModItemTagGenerator;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.tool.SpiritCompassItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class SpiritCompassCombineRecipe extends CustomRecipe {

    private static final Map<ResourceLocation, String> STONE_COLOR_MAP = Map.of(
            GenesisItems.BLUE_CRYSTAL_SHARD.getId(), "water",
            GenesisItems.RED_CRYSTAL_SHARD.getId(), "fire",
            GenesisItems.CITRINE_SHARD.getId(), "earth",
            GenesisItems.WIND_STONE.getId(), "storm",
            GenesisItems.LIGHTING_CRYSTAL_SHARD.getId(), "lightning",
            GenesisItems.GREEN_AMBER.getId(), "plants",
            GenesisItems.ICE_FLOWER_SHARD.getId(), "ice"
    );

    private static final Map<String, List<String>> COLOR_TO_BLOCKS = Map.of(
            "water", List.of("genesis:blue_crystal_cluster"),
            "fire", List.of("genesis:red_crystal_cluster"),
            "earth", List.of("genesis:citrine_cluster"),
            "storm", List.of("genesis:wind_stone_cluster"),
            "lightning", List.of("genesis:lighting_crystal_cluster"),
            "plants", List.of("genesis:green_amber_cluster"),
            "ice", List.of("genesis:ice_flower_cluster")
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
            } else {
                return false;
            }
        }

        return compassCount == 1 && stoneCount == 1;
    }

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

        String color = STONE_COLOR_MAP.getOrDefault(ForgeRegistries.ITEMS.getKey(stone.getItem()), "water");

        // 기본값을 블록으로 변경
        String targetBlockKey = "genesis:blue_crystal_cluster";
        List<String> blocks = COLOR_TO_BLOCKS.get(color);

        if (blocks != null && !blocks.isEmpty()) {
            targetBlockKey = String.join(",", blocks);
        }

        ItemStack result = compass.copy();
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, color);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_TARGET, targetBlockKey);

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(GenesisItems.SPIRIT_COMPASS.get()) || stack.is(ModItemTagGenerator.SPIRIT_STONES)) {
                remains.set(i, ItemStack.EMPTY);
            }
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
        return GenesisRecipeSerializers.SPIRIT_COMPASS_COMBINE.get();
    }
}