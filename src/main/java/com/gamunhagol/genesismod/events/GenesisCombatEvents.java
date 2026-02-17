package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisCombatEvents {
    private static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");
    private static final String KEY_EXTRA_HOLY = "GenesisExtraHoly";
    private static final String KEY_EXTRA_DESTRUCTION = "GenesisExtraDestruction";

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
            float holy = stats.getHolyDamage();
            float destruction = stats.getDestructionDamage();
            if (holy <= 0 && destruction <= 0) return;

            int insertIndex = event.getToolTip().size();
            for (int i = 0; i < event.getToolTip().size(); i++) {
                String content = event.getToolTip().get(i).getString();
                if (content.contains("공격 피해") || content.contains("Attack Damage")) {
                    insertIndex = i + 1;
                    break;
                }
            }
            if (destruction > 0) event.getToolTip().add(insertIndex, Component.translatable("tooltip.genesis.destruction_damage", String.format(" %.1f", destruction)).withStyle(ChatFormatting.DARK_RED));
            if (holy > 0) event.getToolTip().add(insertIndex, Component.translatable("tooltip.genesis.holy_damage", String.format(" %.1f", holy)).withStyle(ChatFormatting.YELLOW));
        });
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();

        if (attackerEntity instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();

            // 1. JSON 데이터가 있는 경우 (신규 시스템)
            if (WeaponDataManager.hasData(weapon.getItem())) {
                // 물리 대미지 적용
                float phys = WeaponRequirementHelper.getPhysicalDamage(player, weapon, event.getAmount());
                event.setAmount(phys);

                // 속성 대미지 예약 (마법, 화염, 번개, 동상, 신성, 파괴)
                String[] types = {"magic", "fire", "lightning", "frost", "holy", "destruction"};
                for (String type : types) {
                    float dmg = WeaponRequirementHelper.calculateElementalDamage(player, weapon, type);
                    if (dmg > 0) {
                        // 키 예시: GenesisExtraMagic, GenesisExtraFire ...
                        String key = "GenesisExtra" + capitalize(type);
                        target.getPersistentData().putFloat(key, dmg);
                    }
                }
            }
            // 2. JSON 데이터가 없는 경우 (기존 시스템 유지)
            else {
                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    float baseDamage = event.getAmount();
                    float bonusRate = 0.0f;
                    bonusRate += StatApplier.calculateScaling(stats.getStrength());
                    bonusRate += StatApplier.calculateScaling(stats.getDexterity()) * 0.5f;

                    if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                        bonusRate += StatApplier.calculateScaling(stats.getIntelligence());
                    }
                    if (event.getSource().is(GenesisDamageTypes.HOLY)) {
                        bonusRate += StatApplier.calculateScaling(stats.getFaith());
                    }
                    event.setAmount(baseDamage + (baseDamage * bonusRate));
                });

                // 구형 Capability 데이터 사용 (호환성)
                weapon.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(wStats -> {
                    if (wStats.getDestructionDamage() > 0) {
                        target.getPersistentData().putFloat("GenesisExtraDestruction", wStats.getDestructionDamage());
                    }
                    // 신성 등 다른 속성도 필요하면 여기에 추가
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;
        LivingEntity target = event.getEntity();
        float finalDamage = event.getAmount();

        //  예약된 속성 대미지 처리
        String[] types = {"Magic", "Fire", "Lightning", "Frost", "Holy", "Destruction"};

        for (String type : types) {
            String key = "GenesisExtra" + type;
            if (target.getPersistentData().contains(key)) {
                float extraDmg = target.getPersistentData().getFloat(key);

                // 속성별 방어력 계산 등 추가 로직
                if (type.equals("Holy")) extraDmg = calculateHolyDamage(target, extraDmg);
                else if (type.equals("Magic")) extraDmg = calculateMagicDamage(target, extraDmg);

                // 파괴(Destruction)는 최대 체력 감소 효과 적용
                if (type.equals("Destruction")) applyDestructionEffect(target, extraDmg);

                finalDamage += extraDmg;
                target.getPersistentData().remove(key); // 사용 후 삭제
            }
        }

        //  기존 파괴 효과 처리 (소스가 직접 파괴 속성일 때)
        if (event.getSource().is(GenesisDamageTypes.DESTRUCTION)) {
            applyDestructionEffect(target, event.getAmount());
        }

        event.setAmount(finalDamage);
    }

    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        removeDestructionEffect(event.getEntity());
    }


    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // --- Helper Methods ---
    private static float calculateHolyDamage(LivingEntity target, float damage) {
        AttributeInstance holyDef = target.getAttribute(GenesisAttributes.HOLY_DEFENSE.get());
        if (holyDef != null && holyDef.getValue() > 0) {
            float reductionMultiplier = (float) (1.0 - (holyDef.getValue() / (holyDef.getValue() + 30.0)));
            float result = damage * reductionMultiplier;
            return (result < 0.5f && damage > 0) ? 0.5f : result;
        }
        return damage;
    }

    private static float calculateMagicDamage(LivingEntity target, float damage) {
        AttributeInstance magicDef = target.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
        if (magicDef != null && magicDef.getValue() > 0) {
            float reductionMultiplier = (float) (1.0 - (magicDef.getValue() / (magicDef.getValue() + 30.0)));
            float result = damage * reductionMultiplier;
            return (result < 0.5f && damage > 0) ? 0.5f : result;
        }
        return damage;
    }

    private static void applyDestructionEffect(LivingEntity entity, float damageAmount) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        double currentModifierValue = 0;
        AttributeModifier existingMod = maxHealthAttr.getModifier(DESTRUCTION_HP_MOD_UUID);
        if (existingMod != null) {
            currentModifierValue = existingMod.getAmount();
            maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        }

        double newReductionValue = currentModifierValue - damageAmount;
        if (maxHealthAttr.getBaseValue() + newReductionValue < 1.0D) newReductionValue = -maxHealthAttr.getBaseValue() + 1.0D;

        maxHealthAttr.addPermanentModifier(new AttributeModifier(DESTRUCTION_HP_MOD_UUID, "Destruction Max HP Reduction", newReductionValue, AttributeModifier.Operation.ADDITION));
        if (entity instanceof ServerPlayer sp) sp.getHealth();
        entity.getPersistentData().putLong("GenesisDestructionEndTick", entity.level().getGameTime() + 144000L);
    }

    public static void removeDestructionEffect(LivingEntity entity) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        entity.getPersistentData().remove("GenesisDestructionEndTick");
    }
}