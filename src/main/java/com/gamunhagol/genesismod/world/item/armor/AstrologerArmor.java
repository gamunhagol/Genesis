package com.gamunhagol.genesismod.world.item.armor;

import com.gamunhagol.genesismod.client.event.RegisterModels;
import com.gamunhagol.genesismod.world.item.GenesisArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class AstrologerArmor extends GenesisArmorItem {
    public AstrologerArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> playerModel) {
                RegisterModels.checkForInitModels();
                return switch (armorSlot) {
                    case HEAD -> RegisterModels.CLOTH_HELMET_MODEL;
                    case CHEST -> RegisterModels.CLOTH_CHESTPLATE_MODEL;
                    case LEGS -> RegisterModels.CLOTH_LEGGINGS_MODEL;
                    case FEET -> RegisterModels.CLOTH_BOOTS_MODEL;
                    default -> null;
                };
            }
        });
    }
}
