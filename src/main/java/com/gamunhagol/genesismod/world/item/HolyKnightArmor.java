package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.client.event.RegisterModels;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class HolyKnightArmor extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("DE1E515-6000-0000-0000-00000004005"), // FEET
            UUID.fromString("DE1E515-6000-0000-0000-00000003005"), // LEGS
            UUID.fromString("DE1E515-6000-0000-0000-00000002005"), // CHEST
            UUID.fromString("DE1E515-6000-0000-0000-00000001005")  // HEAD
    };

    private static final UUID[] HOLY_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("FE1E515-7000-0000-0000-00000004005"), // FEET
            UUID.fromString("FE1E515-7000-0000-0000-00000003005"), // LEGS
            UUID.fromString("FE1E515-7000-0000-0000-00000002005"), // CHEST
            UUID.fromString("FE1E515-7000-0000-0000-00000001005")  // HEAD
    };

    public HolyKnightArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            if (this.getMaterial() instanceof GenesisArmorMaterials genesisMaterial) {
                float magicDefense = genesisMaterial.getMagicDefense();
                if (magicDefense != 0) {
                    builder.put(GenesisAttributes.MAGIC_DEFENSE.get(),
                            new AttributeModifier(
                                    ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()],
                                    "Magic defense",
                                    magicDefense,
                                    AttributeModifier.Operation.ADDITION));
                }

                float holyDefense = genesisMaterial.getHolyDefense();
                if (holyDefense != 0) {
                    builder.put(GenesisAttributes.HOLY_DEFENSE.get(),
                            new AttributeModifier(HOLY_MODIFIER_UUID_PER_SLOT[slot.getIndex()],
                                    "Holy defense",
                                    holyDefense,
                                    AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return builder.build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> playerModel) {
                RegisterModels.checkForInitModels();
                return switch (armorSlot){

                    case HEAD -> RegisterModels.HOLY_KNIGHT_HELMET_MODEL;
                    case CHEST -> RegisterModels.HOLY_KNIGHT_CHESTPLATE_MODEL;
                    case LEGS -> RegisterModels.HOLY_KNIGHT_LEGGINGS_MODEL;
                    case FEET -> RegisterModels.HOLY_KNIGHT_BOOTS_MODEL;

                    default -> null;
                };

            }
        });
    }

}