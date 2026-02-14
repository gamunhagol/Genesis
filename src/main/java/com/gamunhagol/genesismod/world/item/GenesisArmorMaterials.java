package com.gamunhagol.genesismod.world.item;


import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;


public enum GenesisArmorMaterials implements ArmorMaterial {
    PADDED_CHAIN("padded_chain", 17, new int[]{1, 4, 5, 2}, 12, SoundEvents.ARMOR_EQUIP_IRON,
            1.0F, 0.0F,0.0f, 0.0f, () -> {return Ingredient.of(Items.CHAIN);
    }),
    ELVENIA("elvenia", 30, new int[]{2, 5, 6, 2}, 25, SoundEvents.ARMOR_EQUIP_IRON,
            1.0F, 0.0F,0.5f,0.0f, () -> {return Ingredient.of(GenesisItems.ELVENIA_INGOT.get());
    }),
    ANCIENT_ELVENIA("ancient_elvenia", 48, new int[]{3, 6, 8, 3}, 30, SoundEvents.ARMOR_EQUIP_IRON,
            3.0F, 0.0F,1.0f,0.0f, () -> {return Ingredient.of(GenesisItems.ANCIENT_ELVENIA_INGOT.get());
    }),
    PEWRIESE("pewriese", 62, new int[]{4, 7, 9, 4}, 12, SoundEvents.ARMOR_EQUIP_IRON,
            3.0F, 0.0F,0.0f,0.0f, () -> {return Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get());
    }),
    PEWRIESE_PLATE("pewriese_plate", 85, new int[]{6, 9, 11, 6}, 12, SoundEvents.ARMOR_EQUIP_IRON,
            4.0F, 0.4F,0.0f,0.0f, () -> {return Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get());
    }),
    HOLY_KNIGHT("holy_knight", 132, new int[]{6, 8, 10, 5}, 24, SoundEvents.ARMOR_EQUIP_IRON,
            6.0F, 0.2F,0.0f,0.5f, () -> {return Ingredient.of(GenesisItems.PYULITELA.get());
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
    private final float magicDefense;
    private final float holyDefense;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    GenesisArmorMaterials(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn,
                          SoundEvent equipSoundIn, float toughness, float knockbackResistance,
                          float magicDefense,float holyDefense,
                          Supplier<Ingredient> repairMaterialSupplier) {
        this.name = nameIn;
        this.durabilityMultiplier = maxDamageFactorIn;
        this.damageReductionAmountArray = damageReductionAmountsIn;
        this.enchantability = enchantabilityIn;
        this.soundEvent = equipSoundIn;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.magicDefense = magicDefense;
        this.holyDefense = holyDefense;
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

    public float getMagicDefense() {
        return this.magicDefense;
    }

    public float getHolyDefense() { return this.holyDefense;
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