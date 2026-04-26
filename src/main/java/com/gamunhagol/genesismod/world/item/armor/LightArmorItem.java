package com.gamunhagol.genesismod.world.item.armor;

import com.gamunhagol.genesismod.world.item.GenesisArmorItem;
import com.gamunhagol.genesismod.world.item.GenesisArmorMaterials;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class LightArmorItem extends GenesisArmorItem {

    // 세트 효과를 위한 맵 (이 클래스만의 고유 로직이므로 유지)
    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
                    .put(GenesisArmorMaterials.PADDED_CHAIN, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0,
                            false, false, true)).build();

    public LightArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof LivingEntity living) {

            // 20틱(1초)마다 한 번만 검사 (최적화)
            if (living.tickCount % 20 != 0) return;

            if (isEquipped(living, stack)) {
                ArmorMaterial myMaterial = this.getMaterial();
                MobEffectInstance effect = MATERIAL_TO_EFFECT_MAP.get(myMaterial);

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

    private boolean hasFullSet(LivingEntity living, ArmorMaterial material) {
        EquipmentSlot[] armorSlots = {
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        };

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = living.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem armor) || armor.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

    private void addStatusEffect(LivingEntity living, MobEffectInstance effect) {
        MobEffectInstance currentEffect = living.getEffect(effect.getEffect());
        // 5초 미만 남았을 때만 리필하여 패킷 낭비 방지
        if (currentEffect == null || currentEffect.getDuration() < 100) {
            living.addEffect(new MobEffectInstance(effect));
        }
    }
}