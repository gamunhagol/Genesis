package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.content.ReinforceManager;
import com.gamunhagol.genesismod.world.capability.weapon.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ReinforceRecipeMaker {

    public static List<IJeiAnvilRecipe> getAnvilRecipes(IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        for (Item item : ForgeRegistries.ITEMS) {
            if (!WeaponDataManager.hasData(item)) continue;

            WeaponStatData data = WeaponDataManager.get(item);

            Item lastMaterial = null;
            List<ItemStack> leftInputs = new ArrayList<>();
            List<ItemStack> outputs = new ArrayList<>();

            int maxLevel = ReinforceManager.getMaxLevel(data.isSpecial());

            for (int currentLevel = 0; currentLevel < maxLevel; currentLevel++) {
                int nextLevel = currentLevel + 1;
                Item material = data.isSpecial()
                        ? ReinforceManager.getSpecialMaterial(nextLevel)
                        : ReinforceManager.getStandardMaterial(nextLevel);

                if (lastMaterial != null && material != lastMaterial) {
                    recipes.add(factory.createAnvilRecipe(
                            new ArrayList<>(leftInputs),
                            List.of(new ItemStack(lastMaterial)),
                            new ArrayList<>(outputs)
                    ));
                    leftInputs.clear();
                    outputs.clear();
                }

                ItemStack leftStack = new ItemStack(item);
                applyLevel(leftStack, currentLevel);
                leftInputs.add(leftStack);

                ItemStack outputStack = new ItemStack(item);
                applyLevel(outputStack, nextLevel);
                outputs.add(outputStack);

                lastMaterial = material;

                if (nextLevel == maxLevel) {
                    recipes.add(factory.createAnvilRecipe(
                            leftInputs,
                            List.of(new ItemStack(material)),
                            outputs
                    ));
                }
            }
        }
        return recipes;
    }

    private static void applyLevel(ItemStack stack, int level) {
        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(cap -> {
            cap.setReinforceLevel(level);
        });

    }
}