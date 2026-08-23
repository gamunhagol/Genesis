package com.gamunhagol.genesismod.world.effect;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GenesisMod.MODID);

    public static final RegistryObject<MobEffect> FROSTBITE = MOB_EFFECTS.register("frostbite",
            FrostbiteEffect::new);
    public static final RegistryObject<MobEffect> ELECTRIC_SHOCK = MOB_EFFECTS.register("electric_shock",
            ElectricShockEffect::new);
    public static final RegistryObject<MobEffect> LIGHTNING_ROD = MOB_EFFECTS.register("lightning_rod",
            LightningRodEffect::new);
    public static final RegistryObject<MobEffect> SOUL_SCREAM = MOB_EFFECTS.register("soul_scream",
            SoulScreamEffect::new);
}