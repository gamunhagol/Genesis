package com.gamunhagol.genesismod.init.attributes;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GenesisMod.MODID);


    public static final RegistryObject<Attribute> MAGIC_DEFENSE = ATTRIBUTES.register("magic_defense",
            () -> new RangedAttribute("attribute.name.genesis.magic_defense", 0.0D, 0.0D, 1024.0D)
                    .setSyncable(true));

    public static final RegistryObject<Attribute> HOLY_DEFENSE = ATTRIBUTES.register("holy_defense",
            () -> new RangedAttribute("attribute.name.genesis.holy_defense", 0.0D, 0.0D, 1000.0D).setSyncable(true));

    public static final RegistryObject<Attribute> HOLY_DAMAGE = ATTRIBUTES.register("holy_damage",
            () -> new RangedAttribute("attribute.name.genesis.holy_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> DESTRUCTION_DAMAGE = ATTRIBUTES.register("destruction_damage",
            () -> new RangedAttribute("attribute.name.genesis.destruction_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));


    public static final RegistryObject<Attribute> VIGOR = registerStat("vigor", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> MIND = registerStat("mind", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> ENDURANCE = registerStat("endurance", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> STRENGTH = registerStat("strength", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> DEXTERITY = registerStat("dexterity", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> INTELLIGENCE = registerStat("intelligence", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> FAITH = registerStat("faith", 0.0D, 100.0D);
    public static final RegistryObject<Attribute> ARCANE = registerStat("arcane", 0.0D, 100.0D);

    private static RegistryObject<Attribute> registerStat(String name, double base, double max) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute.name.genesis." + name, base, 0.0D, max).setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
