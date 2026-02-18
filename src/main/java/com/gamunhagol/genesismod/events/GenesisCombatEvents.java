package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
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
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisCombatEvents {
    private static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
            float holy = stats.getHolyDamage();
            float destruction = stats.getDestructionDamage();
            // JSON 기반 아이템이라면 JSON 데이터도 툴팁에 표시
            if (WeaponDataManager.hasData(stack.getItem())) {
                WeaponStatData data = WeaponDataManager.get(stack.getItem());
                if (data.baseDestruction() > 0) destruction = data.baseDestruction();
                if (data.baseHoly() > 0) holy = data.baseHoly();
            }

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
        Entity sourceEntity = event.getSource().getDirectEntity(); // 직접 때린 엔티티 (화살, 플레이어 등)
        Entity attackerEntity = event.getSource().getEntity();   // 공격의 주체 (플레이어)

        DamageSnapshot snapshot = null;

        // ==========================================================
        // 1. 투사체(화살, 삼지창 등) 공격 확인 -> 저장된 스냅샷 사용
        // ==========================================================
        if (sourceEntity != null && sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).isPresent()) {
            var cap = sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).orElse(null);
            if (cap != null && !cap.getSnapshot().isEmpty()) {
                snapshot = cap.getSnapshot();
            }
        }

        // ==========================================================
        // 2. 플레이어의 근접 공격 확인 -> 실시간 JSON 데이터 계산
        // (단, 투사체 공격이 아닐 때만 진입)
        // ==========================================================
        else if (attackerEntity instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();

            if (WeaponDataManager.hasData(weapon.getItem())) {
                // [예외] 활이나 석궁으로 직접 때리는 경우 (Bow Melee)
                // JSON 데이터를 무시하고 바닐라 대미지를 적용하기 위해 snapshot을 null로 둠
                if (weapon.getItem() instanceof BowItem || weapon.getItem() instanceof CrossbowItem) {
                    return;
                }

                // 일반 근접 무기 계산
                snapshot = WeaponRequirementHelper.calculateTotalDamage(player, weapon, event.getAmount());
            }
        }

        // ==========================================================
        // 3. 스냅샷 적용 (1번 혹은 2번에서 스냅샷이 생성된 경우)
        // ==========================================================
        if (snapshot != null && !snapshot.isEmpty()) {
            // 물리 대미지 덮어쓰기
            if (snapshot.physical() > 0) {
                event.setAmount(snapshot.physical());
            }
            // 속성 대미지 마커 부착
            applyElementalMarkers(target, snapshot);
        }

        // ==========================================================
        // 레거시(구형) 시스템 지원
        // 스냅샷이 없고(JSON 데이터 X), 공격자가 플레이어인 경우
        // ==========================================================
        else if (attackerEntity instanceof Player player && snapshot == null) {

            // 기존 스탯 보정 로직
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

            // 구형 Capability 데이터 사용 (파괴 속성 등)
            ItemStack weapon = player.getMainHandItem();
            weapon.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(wStats -> {
                if (wStats.getDestructionDamage() > 0) {
                    target.getPersistentData().putFloat("GenesisExtraDestruction", wStats.getDestructionDamage());
                }
            });
        }
    }

    private static void applyElementalMarkers(LivingEntity target, DamageSnapshot snapshot) {
        if (snapshot.magic() > 0) target.getPersistentData().putFloat("GenesisExtraMagic", snapshot.magic());
        if (snapshot.fire() > 0) target.getPersistentData().putFloat("GenesisExtraFire", snapshot.fire());
        if (snapshot.lightning() > 0) target.getPersistentData().putFloat("GenesisExtraLightning", snapshot.lightning());
        if (snapshot.frost() > 0) target.getPersistentData().putFloat("GenesisExtraFrost", snapshot.frost());
        if (snapshot.holy() > 0) target.getPersistentData().putFloat("GenesisExtraHoly", snapshot.holy());
        if (snapshot.destruction() > 0) target.getPersistentData().putFloat("GenesisExtraDestruction", snapshot.destruction());
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;
        LivingEntity target = event.getEntity();
        float finalDamage = event.getAmount();

        // 예약된 속성 대미지 터트리기
        String[] types = {"Magic", "Fire", "Lightning", "Frost", "Holy", "Destruction"};

        for (String type : types) {
            String key = "GenesisExtra" + type;
            if (target.getPersistentData().contains(key)) {
                float extraDmg = target.getPersistentData().getFloat(key);

                // 속성별 방어력 계산
                if (type.equals("Holy")) extraDmg = calculateHolyDamage(target, extraDmg);
                else if (type.equals("Magic")) extraDmg = calculateMagicDamage(target, extraDmg);

                // 파괴(Destruction)는 최대 체력 감소 효과 적용
                if (type.equals("Destruction")) applyDestructionEffect(target, extraDmg);

                finalDamage += extraDmg;
                target.getPersistentData().remove(key); // 사용 후 삭제
            }
        }

        // 소스가 직접 파괴 속성일 때 (예: 환경 대미지 등)
        if (event.getSource().is(GenesisDamageTypes.DESTRUCTION)) {
            applyDestructionEffect(target, event.getAmount());
        }

        event.setAmount(finalDamage);
    }

    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        removeDestructionEffect(event.getEntity());
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