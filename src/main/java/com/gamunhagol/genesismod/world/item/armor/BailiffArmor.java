package com.gamunhagol.genesismod.world.item.armor;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.world.item.GenesisArmorItem;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

import java.util.UUID;

public class BailiffArmor extends GenesisArmorItem {

    private static final UUID HELMET_FAITH_UUID = UUID.fromString("B11E515-F000-0000-0000-00000001000");

    public BailiffArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        // 1. 부모 클래스(GenesisArmorItem)에 정의된 기본 마법/신성 방어력을 먼저 가져옵니다.
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        // 2. 현재 아이템이 들어가야 할 슬롯과 실제로 장착된 슬롯이 일치하는지 확인합니다.
        if (slot == this.type.getSlot()) {

            if (this.type == Type.HELMET) {
                builder.put(GenesisAttributes.FAITH.get(),
                        new AttributeModifier(HELMET_FAITH_UUID, "Bailiff Helmet Faith Bonus", 2.0, AttributeModifier.Operation.ADDITION));
            }

        }

        return builder.build();
    }
}
