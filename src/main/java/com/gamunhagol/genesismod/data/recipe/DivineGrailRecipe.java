package com.gamunhagol.genesismod.data.recipe;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.DivineGrailItem;
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

    // 1. 조합대에 올린 재료가 유효한지 검사
    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        ItemStack grailStack = ItemStack.EMPTY;
        boolean hasTorchflower = false;
        boolean hasHoney = false;
        boolean hasShard = false;
        boolean hasRemains = false;

        // 인벤토리 스캔
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

        // Case 1: 재충전 (성배 + 꽃 + 꿀)
        // 조건: 현재 횟수가 최대 횟수보다 적어야 함 (꽉 차 있으면 조합 안 됨)
        if (hasTorchflower && hasHoney && !hasShard && !hasRemains) {
            return grailItem.getUses(grailStack) < grailItem.getMaxUses(grailStack);
        }

        // Case 2: 최대 횟수 강화 (성배 + 파편)
        // 조건: 최대 횟수가 16 미만이어야 함
        if (hasShard && !hasTorchflower && !hasHoney && !hasRemains) {
            return grailItem.getMaxUses(grailStack) < 16;
        }

        // Case 3: 회복량 강화 (성배 + 유해)
        // 조건: 강화 레벨이 10 미만이어야 함 (로직상 10강 제한이라고 가정)
        if (hasRemains && !hasTorchflower && !hasHoney && !hasShard) {
            // HealLevel 태그를 직접 확인하거나 헬퍼 메서드 활용
            int currentLevel = grailStack.getOrCreateTag().getInt("HealLevel");
            return currentLevel < 10;
        }

        return false;
    }

    // 2. 결과물 생성
    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack grailStack = ItemStack.EMPTY;
        ItemStack materialStack = ItemStack.EMPTY;
        boolean isRefill = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof DivineGrailItem) {
                grailStack = stack.copy(); // NBT 복사
            } else if (stack.is(Items.TORCHFLOWER)) {
                isRefill = true;
            } else if (!stack.is(Items.HONEY_BOTTLE)) {
                materialStack = stack; // 파편 또는 유해
            }
        }

        if (grailStack.isEmpty()) return ItemStack.EMPTY;
        DivineGrailItem grailItem = (DivineGrailItem) grailStack.getItem();

        // 기능 실행
        if (isRefill) {
            grailItem.refill(grailStack); // 꽉 채움
        } else if (materialStack.is(GenesisItems.FLASK_SHARD.get())) {
            grailItem.upgradeMaxUses(grailStack); // 최대 횟수 +1
        } else if (materialStack.is(GenesisItems.BEAST_REMAINS.get())) {
            grailItem.upgradeHealAmount(grailStack); // 회복량 +1
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