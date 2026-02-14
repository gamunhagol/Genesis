package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;

public class LightArmorItem extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("DE1E515-6000-0000-0000-00000004001"), // FEET
            UUID.fromString("DE1E515-6000-0000-0000-00000003001"), // LEGS
            UUID.fromString("DE1E515-6000-0000-0000-00000002001"), // CHEST
            UUID.fromString("DE1E515-6000-0000-0000-00000001001")  // HEAD
    };

    private static final UUID[] HOLY_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("FE1E515-7000-0000-0000-00000004001"), // FEET
            UUID.fromString("FE1E515-7000-0000-0000-00000003001"), // LEGS
            UUID.fromString("FE1E515-7000-0000-0000-00000002001"), // CHEST
            UUID.fromString("FE1E515-7000-0000-0000-00000001001")  // HEAD
    };

    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
                    .put(GenesisArmorMaterials.PADDED_CHAIN, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0,
                            false, false, true)).build();
    // 팁: 지속시간을 40(2초) -> 200(10초) 정도로 늘리면 깜빡임이 덜합니다.

    public LightArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    // [추가] 마법 방어력 적용 로직
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        // 1. 기본 방어력/강도 (필수)
        builder.putAll(super.getDefaultAttributeModifiers(slot));

        // 2. 마법 방어력 추가
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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // 1. 서버 사이드 + LivingEntity 체크
        if (!level.isClientSide() && entity instanceof LivingEntity living) {

            // [최적화 1] 매 틱마다 검사하면 렉 유발. 20틱(1초)마다 한 번만 검사
            if (living.tickCount % 20 != 0) return;

            // 2. 이 아이템이 실제로 해당 부위에 장착되어 있는지 확인
            if (isEquipped(living, stack)) {

                // [최적화 2] 맵 전체를 돌지 않고, '내 재질'에 해당하는 효과가 있는지만 확인
                ArmorMaterial myMaterial = this.getMaterial();
                MobEffectInstance effect = MATERIAL_TO_EFFECT_MAP.get(myMaterial);

                // 효과가 정의된 재질이고 + 4세트를 다 입었다면
                if (effect != null && hasFullSet(living, myMaterial)) {
                    addStatusEffect(living, effect);
                }
            }
        }
    }

    private boolean isEquipped(LivingEntity entity, ItemStack stack) {
        EquipmentSlot slot = this.getType().getSlot();
        return entity.getItemBySlot(slot) == stack;
    }

    // [최적화 3] 메서드 통합: 재질 확인과 장착 확인을 한 번에 수행
    private boolean hasFullSet(LivingEntity living, ArmorMaterial material) {
        // 검사할 슬롯 목록
        EquipmentSlot[] armorSlots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = living.getItemBySlot(slot);

            // 하나라도 비었거나, ArmorItem이 아니거나, 재질이 다르면 즉시 탈락
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem armor) || armor.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

    private void addStatusEffect(LivingEntity living, MobEffectInstance effect) {
        // 이미 해당 효과가 있고, 지속시간이 충분하면 굳이 덧씌우지 않음 (패킷 절약)
        MobEffectInstance currentEffect = living.getEffect(effect.getEffect());
        if (currentEffect == null || currentEffect.getDuration() < 100) { // 5초 미만 남았을 때 리필
            living.addEffect(new MobEffectInstance(effect));
        }
    }
}