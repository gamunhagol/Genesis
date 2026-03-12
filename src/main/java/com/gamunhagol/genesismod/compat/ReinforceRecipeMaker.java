package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.content.ReinforceManager;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
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

            // 1. 재료별로 입/출력 리스트를 그룹화하기 위한 맵이나 로직
            // 여기서는 간단하게 "사용되는 재료"를 기준으로 그룹을 묶습니다.
            Item lastMaterial = null;
            List<ItemStack> leftInputs = new ArrayList<>();
            List<ItemStack> outputs = new ArrayList<>();

            int maxLevel = ReinforceManager.getMaxLevel(data.isSpecial());

            for (int currentLevel = 0; currentLevel < maxLevel; currentLevel++) {
                int nextLevel = currentLevel + 1;
                Item material = data.isSpecial()
                        ? ReinforceManager.getSpecialMaterial(nextLevel)
                        : ReinforceManager.getStandardMaterial(nextLevel);

                // 재료가 바뀌거나 마지막 레벨이면 지금까지 모인 리스트를 레시피로 등록
                if (lastMaterial != null && material != lastMaterial) {
                    recipes.add(factory.createAnvilRecipe(
                            new ArrayList<>(leftInputs),
                            List.of(new ItemStack(lastMaterial)),
                            new ArrayList<>(outputs)
                    ));
                    leftInputs.clear();
                    outputs.clear();
                }

                // 리스트에 단계별 아이템 추가
                ItemStack leftStack = new ItemStack(item);
                applyLevel(leftStack, currentLevel);
                leftInputs.add(leftStack);

                ItemStack outputStack = new ItemStack(item);
                applyLevel(outputStack, nextLevel);
                outputs.add(outputStack);

                lastMaterial = material;

                // 마지막 단계 처리
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
        // Capability에 데이터 세팅 (JEI 화면에서 툴팁 등에 레벨이 표시되도록 함)
        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(cap -> {
            cap.setReinforceLevel(level);
        });

    }
}