package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.DivineGrailItem;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class DivineGrailRecipeMaker {
    public static List<CraftingRecipe> getRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        recipes.add(createRefillRecipe());       // 1. 리필 레시피
        recipes.add(createMaxUsesRecipe());      // 2. 횟수 증가 레시피
        recipes.add(createHealUpgradeRecipe());  // 3. 회복량 증가 레시피

        return recipes;
    }

    // ─────────────── 1. 리필 (Refill) 보여주기 ───────────────
    private static CraftingRecipe createRefillRecipe() {
        // 입력: 텅 빈 성배병 (보여주기용)
        ItemStack emptyGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());
        ((DivineGrailItem) emptyGrail.getItem()).setUses(emptyGrail, 0); // 0회 남음 설정

        // 결과: 꽉 찬 성배병
        ItemStack fullGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());
        ((DivineGrailItem) fullGrail.getItem()).refill(fullGrail); // Max로 채움

        // 재료 목록: 빈 성배 + 횃불꽃 + 꿀병
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(emptyGrail),
                Ingredient.of(Items.TORCHFLOWER),
                Ingredient.of(Items.HONEY_BOTTLE)
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_refill"),
                "grail_refill",
                CraftingBookCategory.MISC,
                fullGrail,
                inputs
        );
    }

    // ─────────────── 2. 최대 횟수 강화 (Max Uses) 보여주기 ───────────────
    private static CraftingRecipe createMaxUsesRecipe() {
        // 입력: 기본 성배병 (Max 4)
        ItemStack inputGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());

        // 결과: 강화된 성배병 (Max 5)
        ItemStack outputGrail = inputGrail.copy();
        ((DivineGrailItem) outputGrail.getItem()).upgradeMaxUses(outputGrail);

        // 재료 목록: 성배 + 파편
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(inputGrail),
                Ingredient.of(GenesisItems.FLASK_SHARD.get())
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_upgrade_max"),
                "grail_upgrade",
                CraftingBookCategory.MISC,
                outputGrail,
                inputs
        );
    }

    // ─────────────── 3. 회복량 강화 (Heal Amount) 보여주기 ───────────────
    private static CraftingRecipe createHealUpgradeRecipe() {
        // 입력: 기본 성배병 (Heal 10)
        ItemStack inputGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());

        // 결과: 강화된 성배병 (Heal 11)
        ItemStack outputGrail = inputGrail.copy();
        ((DivineGrailItem) outputGrail.getItem()).upgradeHealAmount(outputGrail);

        // 재료 목록: 성배 + 야수의 유해
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(inputGrail),
                Ingredient.of(GenesisItems.BEAST_REMAINS.get())
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_upgrade_heal"),
                "grail_upgrade",
                CraftingBookCategory.MISC,
                outputGrail,
                inputs
        );
    }
}
