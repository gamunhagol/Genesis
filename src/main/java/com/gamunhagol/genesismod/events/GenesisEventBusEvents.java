package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = "genesis", bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put((EntityType)GenesisEntities.COLLECTOR.get(), Collector.createAttributes().build());
        event.put((EntityType)GenesisEntities.COLLECTOR_GUARD.get(), CollectorGuard.createAttributes().build());
    }



}
