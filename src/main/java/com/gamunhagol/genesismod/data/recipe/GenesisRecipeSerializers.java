package com.gamunhagol.genesismod.data.recipe;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GenesisMod.MODID);


    public static final RegistryObject<RecipeSerializer<SpiritCompassCombineRecipe>> SPIRIT_COMPASS_COMBINE =
            SERIALIZERS.register("spirit_compass_combine",
                    () -> new SimpleCraftingRecipeSerializer<>(SpiritCompassCombineRecipe::new));


    public static final RegistryObject<RecipeSerializer<SpiritCompassRemoveRecipe>> SPIRIT_COMPASS_REMOVE =
            SERIALIZERS.register("spirit_compass_remove",
                    () -> new SimpleCraftingRecipeSerializer<>(SpiritCompassRemoveRecipe::new));

    public static final RegistryObject<RecipeSerializer<DivineGrailRecipe>> DIVINE_GRAIL_RECIPE =
            SERIALIZERS.register("divine_grail_crafting",
                    () -> new SimpleCraftingRecipeSerializer<>(DivineGrailRecipe::new));
}

