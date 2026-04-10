package com.gamunhagol.genesismod.events;


import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.projectile.GenesisArrowPatch;
import com.gamunhagol.genesismod.world.capability.item.GenesisGreatBowCapability;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisEpicFightEvents {
    private static final UUID GREAT_BOW_IMPACT_ID = UUID.fromString("f67a8451-2292-492c-8069-4f7f6f966144");


    @SubscribeEvent
    public static void onPresetRegistry(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(GenesisMod.prefix("great_bow_preset"), (item) ->
                CapabilityItem.builder()
                        .category(CapabilityItem.WeaponCategories.RANGED)
                        .constructor(GenesisGreatBowCapability::new)
                        .addStyleAttibutes(CapabilityItem.Styles.RANGED, Pair.of(
                                EpicFightAttributes.IMPACT.get(),
                                new AttributeModifier(GREAT_BOW_IMPACT_ID, "Great Bow Impact", 6.0, AttributeModifier.Operation.ADDITION)
                        ))
        );
    }


    @SubscribeEvent
    public static void onEntityPatchRegistry(EntityPatchRegistryEvent event) {
        event.getTypeEntry().put(GenesisEntities.LARGE_ARROW.get(), (entity) -> GenesisArrowPatch::new);
    }
}