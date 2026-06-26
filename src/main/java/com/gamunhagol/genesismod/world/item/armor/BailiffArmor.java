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

public class BailiffArmor extends GenesisArmorItem {

    private static final UUID HELMET_FAITH_UUID = UUID.fromString("B11E515-F000-0000-0000-00000001000");

    public BailiffArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            if (this.type == Type.HELMET) {
                builder.put(GenesisAttributes.FAITH.get(),
                        new AttributeModifier(HELMET_FAITH_UUID, "Bailiff Helmet Faith Bonus", 1.0, AttributeModifier.Operation.ADDITION));
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

                return switch (armorSlot) {
                    case HEAD -> RegisterModels.BAILIFF_HELMET_MODEL;
                    case CHEST -> RegisterModels.BAILIFF_CHESTPLATE_MODEL;
                    case LEGS -> RegisterModels.BAILIFF_LEGGINGS_MODEL;
                    case FEET -> RegisterModels.BAILIFF_BOOTS_MODEL;
                    default -> null;
                };
            }
        });
    }
}