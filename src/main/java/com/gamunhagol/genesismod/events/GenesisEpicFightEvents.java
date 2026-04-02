package com.gamunhagol.genesismod.events;


import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisEpicFightEvents {
    private static final UUID GREAT_BOW_IMPACT_ID = UUID.fromString("f67a8451-2292-492c-8069-4f7f6f966144");

    /**
     * RangedWeaponCapability의 protected 생성자에 접근하기 위한 브릿지 클래스입니다.
     */
    public static class GenesisRangedCapability extends RangedWeaponCapability {
        public GenesisRangedCapability(CapabilityItem.Builder builder) {
            super(builder);
        }
    }

    @SubscribeEvent
    public static void onPresetRegistry(WeaponCapabilityPresetRegistryEvent event) {
        // getTypeRegistry -> getTypeEntry 로 수정
        event.getTypeEntry().put(GenesisMod.prefix("great_bow_preset"), (item) ->
                CapabilityItem.builder()
                        .category(CapabilityItem.WeaponCategories.RANGED)
                        .constructor(GenesisRangedCapability::new)
                        .addStyleAttibutes(CapabilityItem.Styles.RANGED, Pair.of(
                                EpicFightAttributes.IMPACT.get(),
                                new AttributeModifier(GREAT_BOW_IMPACT_ID, "Great Bow Impact", 6.0, AttributeModifier.Operation.ADDITION)
                        ))
        );
    }

    public static class GenesisArrowPatch extends ArrowPatch {
        @Override
        public EpicFightDamageSource createEpicFightDamageSource() {
            // 부모 클래스(ArrowPatch)의 기본 설정을 가져온 뒤, StunType만 덮어씌웁니다.
            // .setStunType(StunType.LONG) : 한참 동안 헉헉거리는 경직
            // .setStunType(StunType.KNOCKDOWN) : 뒤로 자빠지는 경직 (파라솔 대궁 느낌)
            return super.createEpicFightDamageSource().setStunType(StunType.KNOCKDOWN);
        }
    }

    @SubscribeEvent
    public static void onEntityPatchRegistry(EntityPatchRegistryEvent event) {
        // 이제 일반 ArrowPatch 대신, 우리가 만든 GenesisArrowPatch를 등록합니다.
        event.getTypeEntry().put(GenesisEntities.LARGE_ARROW.get(), (entity) -> () -> new GenesisArrowPatch());
    }
}