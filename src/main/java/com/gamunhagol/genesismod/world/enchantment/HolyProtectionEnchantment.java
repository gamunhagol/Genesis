package com.gamunhagol.genesismod.world.enchantment;

import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class HolyProtectionEnchantment extends Enchantment {
    public HolyProtectionEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.ARMOR, slots);
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 8;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if (source.is(GenesisDamageTypes.HOLY)) {
            return level * 2;
        }
        return 0;
    }
    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (this == other) return false;

        if (other instanceof ProtectionEnchantment) {
            return false;
        }
        if (other instanceof ElectricProtectionEnchantment ||
                other instanceof FrostbiteProtectionEnchantment ||
                other instanceof MagicProtectionEnchantment) {
            return false;
        }

        return super.checkCompatibility(other);
    }
}