package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.world.capability.spell.ISpellSlot;
import com.gamunhagol.genesismod.world.capability.weapon.IGenesisWeaponStats;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put((EntityType)GenesisEntities.COLLECTOR.get(), Collector.createAttributes().build());
        event.put((EntityType)GenesisEntities.COLLECTOR_GUARD.get(), CollectorGuard.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_HUSK.get(), Husk.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_DROWNED.get(), Drowned.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_SKELETON.get(), Skeleton.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_WITHER_SKELETON.get(), WitherSkeleton.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_STRAY.get(), Stray.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_BLAZE.get(), Blaze.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_VEX.get(), Vex.createAttributes().build());
        event.put((EntityType)GenesisEntities.SUMMONED_WARDEN.get(), Warden.createAttributes().build());
        event.put((EntityType)GenesisEntities.SPELL_GUARDIAN.get(), Guardian.createAttributes().build());
        event.put((EntityType)GenesisEntities.SPELL_ELDER_GUARDIAN.get(), ElderGuardian.createAttributes().build());
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, GenesisAttributes.MAGIC_DEFENSE.get());
            event.add(type, GenesisAttributes.HOLY_DEFENSE.get());
            event.add(type, GenesisAttributes.FIRE_DEFENSE.get());
            event.add(type, GenesisAttributes.FROST_DEFENSE.get());
            event.add(type, GenesisAttributes.LIGHTNING_DEFENSE.get());
        }

        event.add(EntityType.PLAYER, GenesisAttributes.VIGOR.get());
        event.add(EntityType.PLAYER, GenesisAttributes.MIND.get());
        event.add(EntityType.PLAYER, GenesisAttributes.ENDURANCE.get());
        event.add(EntityType.PLAYER, GenesisAttributes.STRENGTH.get());
        event.add(EntityType.PLAYER, GenesisAttributes.DEXTERITY.get());
        event.add(EntityType.PLAYER, GenesisAttributes.INTELLIGENCE.get());
        event.add(EntityType.PLAYER, GenesisAttributes.FAITH.get());
        event.add(EntityType.PLAYER, GenesisAttributes.ARCANE.get());
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StatCapability.class);
        event.register(IGenesisWeaponStats.class);
        event.register(ProjectileStatsProvider.ProjectileStats.class);
        event.register(ISpellSlot.class);
    }
}
