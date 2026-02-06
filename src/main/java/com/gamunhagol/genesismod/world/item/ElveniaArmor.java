package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.client.event.RegisterModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ElveniaArmor extends ArmorItem {
    public ElveniaArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> playerModel) {
                RegisterModels.checkForInitModels();
                return switch (armorSlot){

                    case HEAD -> RegisterModels.ELVENIA_HELMET_MODEL;
                    case CHEST -> RegisterModels.ELVENIA_CHESTPLATE_MODEL;
                    case LEGS -> RegisterModels.ELVENIA_LEGGINGS_MODEL;
                    case FEET -> RegisterModels.ELVENIA_BOOTS_MODEL;

                    default -> null;
                };

            }
        });
    }
}
