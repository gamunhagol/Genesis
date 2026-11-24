package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.particle.GenesisTrails;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.skill.GenesisSkills;
import com.gamunhagol.genesismod.world.capability.projectile.ChargedProjectilePatch;
import com.gamunhagol.genesismod.world.capability.projectile.GenesisArrowPatch;
import com.gamunhagol.genesismod.world.capability.item.GenesisGreatBowCapability;
import com.gamunhagol.genesismod.world.capability.projectile.GenesisProjectilePatch;
import com.gamunhagol.genesismod.world.capability.projectile.TrailArrowPatch;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.damagesource.StunType;
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

        event.getTypeEntry().put(GenesisMod.prefix("catalyst_preset"), (item) ->
                WeaponCapability.builder()
                        .category(CapabilityItem.WeaponCategories.NOT_WEAPON)
                        .styleProvider((playerpatch) -> CapabilityItem.Styles.ONE_HAND)
                        .innateSkill(CapabilityItem.Styles.COMMON, (itemstack) -> GenesisSkills.MAGIC_CHARGE.get())
                        .innateSkill(CapabilityItem.Styles.ONE_HAND, (itemstack) -> GenesisSkills.MAGIC_CHARGE.get())
        );
    }

    @SubscribeEvent
    public static void onEntityPatchRegistry(EntityPatchRegistryEvent event) {
        event.getTypeEntry().put(GenesisEntities.LARGE_ARROW.get(),
                (entity) -> () -> new GenesisArrowPatch().setTrail(GenesisTrails.LARGE_ARROW_TRAIL));

        event.getTypeEntry().put(GenesisEntities.MAGIC_GLINT.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.8F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.MAGIC_GLINT_TRAIL));
        event.getTypeEntry().put(GenesisEntities.MAGIC_MIST.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.55F, 0.0F, StunType.SHORT));
        event.getTypeEntry().put(GenesisEntities.MAGIC_PEBBLE.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(1.1F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.MAGIC_PEBBLE_TRAIL));

        event.getTypeEntry().put(GenesisEntities.GREAT_MAGIC_PEBBLE.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(2.4F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.GREAT_MAGIC_PEBBLE_TRAIL));
        event.getTypeEntry().put(GenesisEntities.MAGIC_COMET.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(3.6F, 0.0F, StunType.LONG).setTrail(GenesisTrails.MAGIC_COMET_TRAIL));
        event.getTypeEntry().put(GenesisEntities.SHOOTING_STAR.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(5.1F, 0.0F, StunType.LONG).setTrail(GenesisTrails.SHOOTING_STAR_TRAIL));

        event.getTypeEntry().put(GenesisEntities.MAGIC_METEOR.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(1.1F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.MAGIC_METEOR_TRAIL));
        event.getTypeEntry().put(GenesisEntities.SMALL_MAGIC_METEOR.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.16F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.SMALL_MAGIC_METEOR_TRAIL));

        event.getTypeEntry().put(GenesisEntities.MAGIC_SWORD.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(1.95F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.MAGIC_SWORD_TRAIL));

        event.getTypeEntry().put(GenesisEntities.FALLING_STAR_BULLET.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.16F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.MAGIC_PEBBLE_TRAIL));
        event.getTypeEntry().put(GenesisEntities.GRAND_FALLING_STAR_BULLET.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(2.1F, 0.0F, StunType.LONG).setTrail(GenesisTrails.GRAND_FALLING_STAR_TRAIL));

        event.getTypeEntry().put(GenesisEntities.DIVIDED.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(8.9F, 0.0F, StunType.LONG).setTrail(GenesisTrails.DIVIDED_TRAIL));

        event.getTypeEntry().put(GenesisEntities.STAR_GASP.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.5F, 0.0F, StunType.SHORT));

        event.getTypeEntry().put(GenesisEntities.PEBBLE.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(1.1F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.PEBBLE_TRAIL));
        event.getTypeEntry().put(GenesisEntities.ROCK.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(8.45F, 0.0F, StunType.LONG).setTrail(GenesisTrails.ROCK_TRAIL));
        event.getTypeEntry().put(GenesisEntities.LARGE_ROCK.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(27.3F, 0.0F, StunType.KNOCKDOWN).setTrail(GenesisTrails.LARGE_ROCK_TRAIL));

        event.getTypeEntry().put(GenesisEntities.WATER_DROP.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.03F, 0.0F, StunType.NONE));

        event.getTypeEntry().put(GenesisEntities.WATERSPOUT.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.3F, 0.0F, StunType.SHORT));

        event.getTypeEntry().put(GenesisEntities.WIND_FEATHER.get(),
                (entity) -> () -> new TrailArrowPatch().setTrail(GenesisTrails.WIND_FEATHER_TRAIL));
        event.getTypeEntry().put(GenesisEntities.BLADE_STORM.get(),
                (entity) -> () -> new TrailArrowPatch().setTrail(GenesisTrails.WIND_FEATHER_TRAIL));

        event.getTypeEntry().put(GenesisEntities.STORM_SEGMENT_1.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(0.3F, 0.0F, StunType.SHORT));

        event.getTypeEntry().put(GenesisEntities.LIGHTNING_ARROW.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(2.2F, 0.0F, StunType.SHORT).setTrail(GenesisTrails.LIGHTNING_ARROW_TRAIL));

        event.getTypeEntry().put(GenesisEntities.LIGHTNING_SPEAR.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(5.1F, 0.0F, StunType.LONG).setTrail(GenesisTrails.LIGHTNING_SPEAR_TRAIL));

        event.getTypeEntry().put(GenesisEntities.LIGHTNING_STAKE.get(),
                (entity) -> () -> new GenesisProjectilePatch<>(7.01F, 0.0F, StunType.KNOCKDOWN).setTrail(GenesisTrails.LIGHTNING_SPEAR_TRAIL));

        event.getTypeEntry().put(GenesisEntities.VALA_ARROW.get(),
                (entity) -> () -> new ChargedProjectilePatch<>(5.9F, 0.0F, StunType.LONG).setTrail(GenesisTrails.VALA_ARROW_TRAIL));

    }
}
