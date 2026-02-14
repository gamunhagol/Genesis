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

public class AncientElveniaArmor extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("DE1E515-6000-0000-0000-00000004003"), // FEET
            UUID.fromString("DE1E515-6000-0000-0000-00000003003"), // LEGS
            UUID.fromString("DE1E515-6000-0000-0000-00000002003"), // CHEST
            UUID.fromString("DE1E515-6000-0000-0000-00000001003")  // HEAD
    };

    private static final UUID[] HOLY_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("FE1E515-7000-0000-0000-00000004003"), // FEET
            UUID.fromString("FE1E515-7000-0000-0000-00000003003"), // LEGS
            UUID.fromString("FE1E515-7000-0000-0000-00000002003"), // CHEST
            UUID.fromString("FE1E515-7000-0000-0000-00000001003")  // HEAD
    };

    public AncientElveniaArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    // [핵심 추가] 아이템이 입혀졌을 때 마법 방어력 스탯을 부여하는 메서드
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        // 1. 기본 방어력/강도 추가
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        // 2. 현재 아이템의 슬롯과 일치할 때만 마법 방어력 추가
        if (slot == this.type.getSlot()) {
            if (this.getMaterial() instanceof GenesisArmorMaterials genesisMaterial) {
                float magicDefense = genesisMaterial.getMagicDefense();
                // 마법 방어력이 0이 아닐 때만 (양수든 음수든) 적용
                if (magicDefense != 0) {
                    builder.put(GenesisAttributes.MAGIC_DEFENSE.get(),
                            new AttributeModifier(
                                    ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], // 슬롯별 고유 UUID
                                    "Magic defense",
                                    magicDefense,
                                    AttributeModifier.Operation.ADDITION));
                }

                // 신성 방어력
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


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> playerModel) {
                RegisterModels.checkForInitModels();
                return switch (armorSlot){
                    case HEAD -> RegisterModels.ANCIENT_ELVENIA_HELMET_MODEL;
                    case CHEST -> RegisterModels.ANCIENT_ELVENIA_CHESTPLATE_MODEL;
                    case LEGS -> RegisterModels.ANCIENT_ELVENIA_LEGGINGS_MODEL;
                    case FEET -> RegisterModels.ANCIENT_ELVENIA_BOOTS_MODEL;
                    default -> null;
                };
            }
        });
    }
}