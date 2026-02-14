package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;
import java.util.function.Supplier;

public enum GenesisItemTier implements Tier {
    // [이름](레벨, 내구도, 속도, 기본물리피해, 마법부여, 수리템, [추가속성], [추가피해량])

    // 엘베니아: 물리 + 신성 2.0 추가
    ELVENIA(2, 500, 6.0f, 2.0f, 24,
            () -> Ingredient.of(GenesisItems.ELVENIA_INGOT.get()),
            GenesisDamageTypes.HOLY, 2.0f),

    // 고대 엘베니아: 물리 + 신성 4.0 추가
    ANCIENT_ELVENIA(3, 3121, 8.0f, 3.0f, 28,
            () -> Ingredient.of(GenesisItems.ANCIENT_ELVENIA_INGOT.get()),
            GenesisDamageTypes.HOLY, 4.0f),

    // 퓨리스: 물리 + 파괴 5.0 추가 (최대 체력 감소 효과 동반)
    PEWRIESE(5, 4431, 10.0f, 5.0f, 12,
            () -> Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get()),
            GenesisDamageTypes.DESTRUCTION, 5.0f);

    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    // 우리가 추가한 커스텀 필드
    private final ResourceKey<DamageType> additionalDamageType;
    private final float additionalDamage;

    GenesisItemTier(int level, int uses, float speed, float damage, int enchantmentValue,
                    Supplier<Ingredient> repairIngredient,
                    ResourceKey<DamageType> additionalDamageType, float additionalDamage) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.additionalDamageType = additionalDamageType;
        this.additionalDamage = additionalDamage;
    }

    // --- Tier 인터페이스 구현 ---
    @Override public int getUses() { return this.uses; }
    @Override public float getSpeed() { return this.speed; }
    @Override public float getAttackDamageBonus() { return this.attackDamageBonus; }
    @Override public int getLevel() { return this.level; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }

    // --- 커스텀 메서드 (이벤트에서 사용) ---
    public ResourceKey<DamageType> getAdditionalDamageType() { return this.additionalDamageType; }
    public float getAdditionalDamage() { return this.additionalDamage; }
}