package com.gamunhagol.genesismod.world.item.custom;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, GenesisMod.MODID);

    public static final RegistryObject<Potion> FROSTBITE = POTIONS.register("frostbite",
            () -> new Potion(new MobEffectInstance(GenesisEffects.FROSTBITE.get(), 3600, 0)));
    public static final RegistryObject<Potion> LONG_FROSTBITE = POTIONS.register("long_frostbite",
            () -> new Potion(new MobEffectInstance(GenesisEffects.FROSTBITE.get(), 9600, 0)));
    public static final RegistryObject<Potion> STRONG_FROSTBITE = POTIONS.register("strong_frostbite",
            () -> new Potion(new MobEffectInstance(GenesisEffects.FROSTBITE.get(), 1800, 1)));

    public static final RegistryObject<Potion> ELECTRIC_SHOCK = POTIONS.register("electric_shock",
            () -> new Potion(new MobEffectInstance(GenesisEffects.ELECTRIC_SHOCK.get(), 3600, 0)));
    public static final RegistryObject<Potion> LONG_ELECTRIC_SHOCK = POTIONS.register("long_electric_shock",
            () -> new Potion(new MobEffectInstance(GenesisEffects.ELECTRIC_SHOCK.get(), 9600, 0)));
    public static final RegistryObject<Potion> STRONG_ELECTRIC_SHOCK = POTIONS.register("strong_electric_shock",
            () -> new Potion(new MobEffectInstance(GenesisEffects.ELECTRIC_SHOCK.get(), 1800, 1)));

    public static final RegistryObject<Potion> PARALYSIS = POTIONS.register("paralysis",
            () -> new Potion(new MobEffectInstance(GenesisEffects.PARALYSIS.get(), 120, 0)));
    public static final RegistryObject<Potion> DEEP_FREEZE = POTIONS.register("deep_freeze",
            () -> new Potion(new MobEffectInstance(GenesisEffects.DEEP_FREEZE.get(), 120, 0)));

    public static final RegistryObject<Potion> LIGHTNING_RESISTANCE = POTIONS.register("lightning_resistance",
            () -> new Potion(new MobEffectInstance(GenesisEffects.LIGHTNING_RESISTANCE.get(), 3600, 0)));
    public static final RegistryObject<Potion> LONG_LIGHTNING_RESISTANCE = POTIONS.register("long_lightning_resistance",
            () -> new Potion(new MobEffectInstance(GenesisEffects.LIGHTNING_RESISTANCE.get(), 9600, 0)));

    public static final RegistryObject<Potion> COLD_RESISTANCE = POTIONS.register("cold_resistance",
            () -> new Potion(new MobEffectInstance(GenesisEffects.COLD_RESISTANCE.get(), 3600, 0)));
    public static final RegistryObject<Potion> LONG_COLD_RESISTANCE = POTIONS.register("long_cold_resistance",
            () -> new Potion(new MobEffectInstance(GenesisEffects.COLD_RESISTANCE.get(), 9600, 0)));

    public static final RegistryObject<Potion> SOUL_SCREAM = POTIONS.register("soul_scream",
            () -> new Potion(new MobEffectInstance(GenesisEffects.SOUL_SCREAM.get(), 9600, 0)));
}
