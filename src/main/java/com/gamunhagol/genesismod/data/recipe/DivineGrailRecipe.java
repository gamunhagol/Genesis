package com.gamunhagol.genesismod.data.recipe;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.tool.DivineGrailItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DivineGrailRecipe extends CustomRecipe {

    public DivineGrailRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        ItemStack grailStack = ItemStack.EMPTY;
        boolean hasTorchflower = false;
        boolean hasHoney = false;
        boolean hasShard = false;
        boolean hasRemains = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof DivineGrailItem) {
                if (!grailStack.isEmpty()) return false; // 성배병은 1개만
                grailStack = stack;
            } else if (stack.is(Items.TORCHFLOWER)) {
                hasTorchflower = true;
            } else if (stack.is(Items.HONEY_BOTTLE)) {
                hasHoney = true;
            } else if (stack.is(GenesisItems.FLASK_SHARD.get())) {
                hasShard = true;
            } else if (stack.is(GenesisItems.BEAST_REMAINS.get())) {
                hasRemains = true;
            } else {
                return false; // 그 외 잡동사니 불가
            }
        }

        if (grailStack.isEmpty()) return false;
        DivineGrailItem grailItem = (DivineGrailItem) grailStack.getItem();

        if (hasTorchflower && hasHoney && !hasShard && !hasRemains) {
            return grailItem.getUses(grailStack) < grailItem.getMaxUses(grailStack);
        }

        if (hasShard && !hasTorchflower && !hasHoney && !hasRemains) {
            return grailItem.getMaxUses(grailStack) < 16;
        }

        if (hasRemains && !hasTorchflower && !hasHoney && !hasShard) {
            int currentLevel = grailStack.getOrCreateTag().getInt("HealLevel");
            return currentLevel < 10;
        }

        return false;
    }
    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack grailStack = ItemStack.EMPTY;
        ItemStack materialStack = ItemStack.EMPTY;
        boolean isRefill = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof DivineGrailItem) {
                grailStack = stack.copy();
            } else if (stack.is(Items.TORCHFLOWER)) {
                isRefill = true;
            } else if (!stack.is(Items.HONEY_BOTTLE)) {
                materialStack = stack;
            }
        }

        if (grailStack.isEmpty()) return ItemStack.EMPTY;
        DivineGrailItem grailItem = (DivineGrailItem) grailStack.getItem();

        if (isRefill) {
            grailItem.refill(grailStack);
        } else if (materialStack.is(GenesisItems.FLASK_SHARD.get())) {
            grailItem.upgradeMaxUses(grailStack);
        } else if (materialStack.is(GenesisItems.BEAST_REMAINS.get())) {
            grailItem.upgradeHealAmount(grailStack);
        }

        return grailStack;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GenesisRecipeSerializers.DIVINE_GRAIL_RECIPE.get();
    }
}