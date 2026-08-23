package com.gamunhagol.genesismod.world.enchantment;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, GenesisMod.MODID);

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public static final RegistryObject<Enchantment> ELECTRIC_PROTECTION = ENCHANTMENTS.register("electric_protection",
            () -> new ElectricProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ARMOR_SLOTS));

    public static final RegistryObject<Enchantment> FROSTBITE_PROTECTION = ENCHANTMENTS.register("frostbite_protection",
            () -> new FrostbiteProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ARMOR_SLOTS));

    public static final RegistryObject<Enchantment> MAGIC_PROTECTION = ENCHANTMENTS.register("magic_protection",
            () -> new MagicProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ARMOR_SLOTS));

    public static final RegistryObject<Enchantment> HOLY_PROTECTION = ENCHANTMENTS.register("holy_protection",
            () -> new HolyProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ARMOR_SLOTS));
}