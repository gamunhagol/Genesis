package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncMentalPower;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import com.gamunhagol.genesismod.world.item.GenesisArmorMaterials;
import com.gamunhagol.genesismod.world.item.GenesisItemTier;
import com.gamunhagol.genesismod.world.spawner.CollectorSpawner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisForgeEvents {
    private static final CollectorSpawner COLLECTOR_SPAWNER = new CollectorSpawner();
    private static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E515-9000-0000-0000-00000009001");


    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "stats"), new StatCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            // Capability 처리
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.tick();
                if (stats.getMentalPower() < stats.getMaxMentalPower()) {
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncMentalPower(stats.getMentalPower()),
                            (ServerPlayer) player // event.player 대신 지역변수 player 사용
                    );
                }
            });

            // 최적화: 1초(20틱)마다 검사
            if (player.tickCount % 20 != 0) return;

            // 파괴 피해 지속 시간 체크
            if (player.getPersistentData().contains("GenesisDestructionEndTick")) {
                long endTick = player.getPersistentData().getLong("GenesisDestructionEndTick");
                long currentTick = player.level().getGameTime();

                // 시간이 다 되었으면 효과 해제
                if (currentTick >= endTick) {
                    removeDestructionEffect(player);
                }
            }
        }
    }

    // 공통 해제 로직
    private static void removeDestructionEffect(LivingEntity entity) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        }
        // NBT 데이터 삭제
        entity.getPersistentData().remove("GenesisDestructionEndTick");
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Level.OVERWORLD) {
                COLLECTOR_SPAWNER.tick(serverLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster monster) {
            if (!(monster instanceof CollectorGuard)) {
                monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, CollectorGuard.class, true));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide) return;
        if (entity.tickCount % 20 != 0) return;

        if (hasFullPaddedChainSet(entity)) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
        }
    }

    private static boolean hasFullPaddedChainSet(LivingEntity entity) {
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);

        return isPaddedChain(helmet) && isPaddedChain(chest) && isPaddedChain(legs) && isPaddedChain(boots);
    }

    private static boolean isPaddedChain(ItemStack stack) {
        return !stack.isEmpty() &&
                stack.getItem() instanceof ArmorItem armor &&
                armor.getMaterial() == GenesisArmorMaterials.PADDED_CHAIN;
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player newPlayer = event.getEntity();
            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = newPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDrops(net.minecraftforge.event.entity.living.LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) {
            for (net.minecraft.world.entity.item.ItemEntity itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;

        LivingEntity entity = event.getEntity();
        float originalDamage = event.getAmount();
        float finalDamage = originalDamage;

        var source = event.getSource();

        // =================================================================
        // [1] 파괴(Destruction) 피해 처리 (최상위 우선순위 & 특수 효과)
        // =================================================================
        if (source.is(GenesisDamageTypes.DESTRUCTION)) {
            AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);

            if (maxHealthAttr != null) {
                double currentModifierValue = 0;
                AttributeModifier existingMod = maxHealthAttr.getModifier(DESTRUCTION_HP_MOD_UUID);
                if (existingMod != null) {
                    currentModifierValue = existingMod.getAmount();
                    maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
                }

                double newReductionValue = currentModifierValue - originalDamage;

                if (maxHealthAttr.getBaseValue() + newReductionValue < 1.0D) {
                    newReductionValue = -maxHealthAttr.getBaseValue() + 1.0D;
                }

                maxHealthAttr.addPermanentModifier(new AttributeModifier(
                        DESTRUCTION_HP_MOD_UUID,
                        "Destruction Max HP Reduction",
                        newReductionValue,
                        AttributeModifier.Operation.ADDITION
                ));

                if (entity.getHealth() > entity.getMaxHealth()) {
                    entity.setHealth(entity.getMaxHealth());
                }

                long endTick = entity.level().getGameTime() + 400L; // 2시간 지속(144000L틱)
                entity.getPersistentData().putLong("GenesisDestructionEndTick", endTick);
            }
        }
        // =================================================================
        // [2] 신성(Holy) 방어력 계산 - (else if 로 연결됨!)
        // =================================================================
        else if (event.getSource().is(GenesisDamageTypes.HOLY)) {
            AttributeInstance holyDefenseAttr = entity.getAttribute(GenesisAttributes.HOLY_DEFENSE.get());
            if (holyDefenseAttr != null) {
                double defenseValue = holyDefenseAttr.getValue();

                if (defenseValue > 0) {
                    float reductionMultiplier = (float) (1.0 - (defenseValue / (defenseValue + 30.0)));
                    finalDamage = originalDamage * reductionMultiplier;

                    if (finalDamage < 0.5f && originalDamage > 0) {
                        finalDamage = 0.5f;
                    }
                }
            }
        }
        // =================================================================
        // [3] 마법(Magic) 방어력 계산
        // =================================================================
        else if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            AttributeInstance magicDefenseAttr = entity.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
            if (magicDefenseAttr != null) {
                double defenseValue = magicDefenseAttr.getValue();
                if (defenseValue > 0) {
                    float reductionMultiplier = (float) (1.0 - (defenseValue / (defenseValue + 30.0)));
                    finalDamage = originalDamage * reductionMultiplier;

                    if (finalDamage < 0.5f && originalDamage > 0) {
                        finalDamage = 0.5f;
                    }
                }
            }
        }
        // =================================================================
        // [4] 물리(Physical) 방어력 캡 돌파 계산
        // =================================================================
        else if (!event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            float currentArmor = entity.getArmorValue();
            if (currentArmor > 20) {
                float excessArmor = currentArmor - 20.0f;
                float reductionFactor = excessArmor / (excessArmor + 50.0f);
                finalDamage = originalDamage * (1.0f - reductionFactor);
            }
        }

        if (finalDamage != originalDamage) {
            event.setAmount(finalDamage);
        }
    }

    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        // 사망 시 파괴 효과 완전 해제 (NBT + Modifier 둘 다)
        removeDestructionEffect(event.getEntity());
    }

    @SubscribeEvent
    public static void onWeaponAddDamage(LivingHurtEvent event) {
        // 1. 공격자가 살아있는 엔티티인지 확인
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack heldItem = attacker.getMainHandItem();

            // 2. 공격자가 든 아이템이 티어 아이템인지, 그리고 그 티어가 우리 모드의 티어인지 확인
            if (heldItem.getItem() instanceof TieredItem tieredItem &&
                    tieredItem.getTier() instanceof GenesisItemTier genesisTier) {

                Level level = attacker.level();
                LivingEntity target = event.getEntity();

                // 클라이언트 사이드 실행 방지 및 무한 루프 방지(속성 피해가 또 이벤트를 부르지 않도록)
                if (level.isClientSide) return;

                // 3. 티어에 설정된 추가 피해 정보 가져오기
                ResourceKey<DamageType> extraType = genesisTier.getAdditionalDamageType();
                float extraAmount = genesisTier.getAdditionalDamage();

                if (extraType != null && extraAmount > 0) {
                    // [핵심] 기존 물리 피해는 그대로 두고, 속성 피해를 '추가로' 입힘
                    target.hurt(GenesisDamageTypes.getSource(level, extraType, attacker), extraAmount);
                }
            }
        }
    }
}
