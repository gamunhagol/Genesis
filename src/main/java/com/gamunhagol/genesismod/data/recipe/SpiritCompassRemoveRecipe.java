package com.gamunhagol.genesismod.data.recipe;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SpiritCompassRemoveRecipe extends CustomRecipe {

    public SpiritCompassRemoveRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        boolean found = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.isEmpty()) continue;

            // 정령 나침반만 허용
            if (stack.is(GenesisItems.SPIRIT_COMPASS.get())) {
                // 침이 있는 나침반만 인정
                if (stack.hasTag() && stack.getTag().getBoolean(SpiritCompassItem.KEY_HAS_NEEDLE)) {
                    if (found) return false; // 두 개 이상이면 무효
                    found = true;
                } else {
                    return false; // 침 없는 나침반은 무효
                }
            } else {
                return false; // 다른 아이템 있으면 무효
            }
        }
        return found;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(GenesisItems.SPIRIT_COMPASS.get()) && stack.hasTag()) {
                ItemStack result = stack.copy();
                // NBT 초기화
                result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, false);
                result.getOrCreateTag().remove(SpiritCompassItem.KEY_NEEDLE_TYPE);
                result.getOrCreateTag().remove(SpiritCompassItem.KEY_TARGET);
                result.getOrCreateTag().remove("LodestonePos");
                result.getOrCreateTag().remove("LodestoneDimension");
                result.getOrCreateTag().remove("LodestoneTracked");
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 1;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GenesisRecipeSerializers.SPIRIT_COMPASS_REMOVE.get();
    }
}

