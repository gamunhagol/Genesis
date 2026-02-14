package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        // 모든 LivingEntity(살아있는 것들) 타입을 순회하며 추가
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, GenesisAttributes.MAGIC_DEFENSE.get());
            event.add(type, GenesisAttributes.HOLY_DEFENSE.get());
        }
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StatCapability.class);
    }
}
