package com.gamunhagol.genesismod.world.item.armor;

import com.gamunhagol.genesismod.client.event.RegisterModels;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.world.item.GenesisArmorItem;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class EmbroideredArmor extends GenesisArmorItem {
    public EmbroideredArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    private static final UUID HELMET_FAITH_UUID = UUID.fromString("C11E515-F101-0000-0000-00000001000");

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            if (this.type == Type.HELMET) {
                builder.put(GenesisAttributes.FAITH.get(),
                        new AttributeModifier(HELMET_FAITH_UUID, "Embroidered Veil Faith Bonus", 2.0, AttributeModifier.Operation.ADDITION));
            }
        }
        return builder.build();
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> playerModel) {
                RegisterModels.checkForInitModels();
                if (armorSlot == EquipmentSlot.HEAD) {
                    return RegisterModels.EMBROIDERED_VEIL_MODEL;
                }

                return null;
            }
        });
    }
}
