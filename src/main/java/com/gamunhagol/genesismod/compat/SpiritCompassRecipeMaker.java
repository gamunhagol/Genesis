package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class SpiritCompassRecipeMaker {

    public static List<CraftingRecipe> getRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        recipes.addAll(createCombineRecipes());
        recipes.add(createRemoveRecipe());

        return recipes;
    }

    // --- 강화 레시피 생성 로직 ---
    private static List<CraftingRecipe> createCombineRecipes() {
        List<CraftingRecipe> list = new ArrayList<>();

        // CombineRecipe 클래스에 정의했던 데이터와 동일하게 매칭
        addCombine(list, GenesisItems.BLUE_CRYSTAL_SHARD.get(), "water", "minecraft:monument");
        addCombine(list, GenesisItems.RED_CRYSTAL_SHARD.get(), "fire", "minecraft:fortress");
        addCombine(list, GenesisItems.CITRINE_SHARD.get(), "earth", "minecraft:ancient_city");
        addCombine(list, GenesisItems.WIND_STONE.get(), "storm", "minecraft:pillager_outpost");
        addCombine(list, GenesisItems.LIGHTING_CRYSTAL_SHARD.get(), "lightning", "minecraft:stronghold");
        addCombine(list, GenesisItems.GREEN_AMBER.get(), "plants", "minecraft:jungle_pyramid");
        addCombine(list, GenesisItems.ICE_FLOWER_SHARD.get(), "ice", "minecraft:igloo");

        return list;
    }

    private static void addCombine(List<CraftingRecipe> list, ItemLike stone, String type, String target) {
        ItemStack result = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, type);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_TARGET, target);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(GenesisItems.SPIRIT_COMPASS.get()),
                Ingredient.of(stone)
        );

        list.add(new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_spirit_combine_" + type),
                "spirit_compass_upgrade",
                CraftingBookCategory.MISC,
                result,
                inputs
        ));
    }

    // --- 제거 레시피 생성 로직 ---
    private static ShapelessRecipe createRemoveRecipe() {
        // 입력값: 침이 있는 상태의 예시 나침반 (NBT 설정)
        ItemStack inputCompass = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        inputCompass.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        inputCompass.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, "any");

        // 결과물: 완전히 초기화된 나침반
        ItemStack result = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, false);

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_spirit_remove"),
                "spirit_compass_reset",
                CraftingBookCategory.MISC,
                result,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(inputCompass))
        );
    }
}
