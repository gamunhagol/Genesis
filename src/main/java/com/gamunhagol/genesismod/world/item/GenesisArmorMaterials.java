package com.gamunhagol.genesismod.world.item;


import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;


public enum GenesisArmorMaterials implements ArmorMaterial {
    PEWRIESE("pewriese", 62, new int[]{4, 7, 9, 4}, 12, SoundEvents.ARMOR_EQUIP_IRON,
            3.0F, 0.0F, () -> {return Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get());
    }),
    PEWRIESE_PLATE("pewriese_plate", 85, new int[]{6, 9, 11, 6}, 12, SoundEvents.ARMOR_EQUIP_IRON,
            4.0F, 0.4F, () -> {return Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get());
    }),
    HOLY_KNIGHT("holy_knight", 132, new int[]{6, 8, 10, 5}, 24, SoundEvents.ARMOR_EQUIP_IRON,
            6.0F, 0.2F, () -> {return Ingredient.of(GenesisItems.PYULITELA.get());
    })
    ;
    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int enchantability;
    private final int durabilityMultiplier;
    private final int[] damageReductionAmountArray;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    GenesisArmorMaterials(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn,
                          SoundEvent equipSoundIn, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = nameIn;
        this.durabilityMultiplier = maxDamageFactorIn;
        this.damageReductionAmountArray = damageReductionAmountsIn;
        this.enchantability = enchantabilityIn;
        this.soundEvent = equipSoundIn;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialSupplier);
    }

    @Override
    public String getName() {
        return GenesisMod.MODID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.damageReductionAmountArray[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}