package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.main.GenesisMod;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;

import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.List;

@JeiPlugin
public class GenesisJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(GenesisMod.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<CraftingRecipe> recipes = SpiritCompassRecipeMaker.getRecipes();
        registration.addRecipes(mezz.jei.api.constants.RecipeTypes.CRAFTING, recipes);
    }
}