package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.gui.MentalPowerGui;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncMentalPower;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import com.gamunhagol.genesismod.world.item.GenesisArmorMaterials;
import com.gamunhagol.genesismod.world.spawner.CollectorSpawner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisForgeEvents {
    private static final CollectorSpawner COLLECTOR_SPAWNER = new CollectorSpawner();


    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {

            event.addCapability(new ResourceLocation(GenesisMod.MODID, "stats"), new StatCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            // [문제점] 람다식 내부에서 변수 접근 문제 (ServerPlayer 캐스팅 등)
            event.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.tick();

                // [수정 필요] 매 틱마다 보내는 것보다, 값이 변했을 때만 보내는 로직이 더 안전합니다.
                // 현재 코드도 나쁘진 않으나, 아래처럼 다듬는 게 좋습니다.
                if (stats.getMentalPower() < stats.getMaxMentalPower()) {
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncMentalPower(stats.getMentalPower()),
                            (ServerPlayer) event.player
                    );
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        // 서버 월드 틱의 마지막 시점에만 실행
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Level.OVERWORLD) {
                COLLECTOR_SPAWNER.tick(serverLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        // 1. 일반적인 몬스터(스켈레톤, 거미, 좀비 등)와 약탈자(Pillager, Vindicator 등)인지 확인
        if (entity instanceof Monster monster) {
            // 단, 호위병 자기 자신은 제외해야 서로 싸우지 않습니다.
            if (!(monster instanceof CollectorGuard)) {
                monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, CollectorGuard.class, true));
            }
        }
    }

    // [추가됨] 살아있는 모든 엔티티(좀비, 플레이어, 주민 등)가 매 틱마다 실행하는 이벤트
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        // 1. 클라이언트면 무시 (서버에서만 처리)
        if (level.isClientSide) return;

        // 2. 1초(20틱)에 한 번만 검사 (최적화) -> 매 틱 검사하면 렉 걸릴 수 있음
        if (entity.tickCount % 20 != 0) return;

        // 3. Padded Chain 풀세트인지 확인
        if (hasFullPaddedChainSet(entity)) {
            // 효과 부여 (이미 있으면 시간만 갱신됨)
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
        }
    }

    // [도우미 메서드] 패디드 체인 풀세트 검사기
    private static boolean hasFullPaddedChainSet(LivingEntity entity) {
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);

        return isPaddedChain(helmet) && isPaddedChain(chest) && isPaddedChain(legs) && isPaddedChain(boots);
    }

    // [도우미 메서드] 아이템 하나가 패디드 체인인지 확인
    private static boolean isPaddedChain(ItemStack stack) {
        return !stack.isEmpty() &&
                stack.getItem() instanceof ArmorItem armor &&
                armor.getMaterial() == GenesisArmorMaterials.PADDED_CHAIN;
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // 부활한 플레이어 객체
            Player newPlayer = event.getEntity();

            // 만약 keepInventory가 켜져 있거나, 아이템 보존 로직이 작동했다면
            // 부활한 인벤토리에 들어있는 성배병을 찾아 리필합니다.
            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = newPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }

    /**
     * 상황 2: 플레이어가 죽어서 아이템을 바닥에 떨어뜨릴 때
     */
    @SubscribeEvent
    public static void onPlayerDrops(net.minecraftforge.event.entity.living.LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) {
            // 바닥에 떨어지는 아이템 엔티티 리스트를 확인
            for (net.minecraft.world.entity.item.ItemEntity itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();

                // 성배병인 경우, 떨어진 아이템 자체를 리필 상태로 변경
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }



    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        // 이미 대미지가 없거나(0), 다른 모드에 의해 취소된 경우 계산하지 않음
        if (event.getAmount() <= 0) return;

        LivingEntity entity = event.getEntity();
        float originalDamage = event.getAmount(); // 다른 모드가 수정한 대미지를 가져옴
        float finalDamage = originalDamage;

        // 1. 마법 방어력 계산 (우선 순위)
        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            AttributeInstance magicDefenseAttr = entity.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
            if (magicDefenseAttr != null) {
                double defenseValue = magicDefenseAttr.getValue();
                if (defenseValue > 0) {
                    // 마법 방어력 공식 (분모 20.0)
                    // 공식: 1 - (방어력 / (방어력 + 20))
                    float reductionMultiplier = (float) (1.0 - (defenseValue / (defenseValue + 30.0)));
                    finalDamage = originalDamage * reductionMultiplier;

                    // 최소 대미지 보장 (0.5f)
                    if (finalDamage < 0.5f && originalDamage > 0) {
                        finalDamage = 0.5f;
                    }
                }
            }
        }
        // 2. 물리 방어력 캡 돌파 계산 (마법이 아니고, 방어 무시가 아닐 때만)
        else if (!event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            float currentArmor = entity.getArmorValue();

            // 바닐라 캡(20)을 넘는 경우에만 추가 감소 적용
            if (currentArmor > 20) {
                float excessArmor = currentArmor - 20.0f;

                // ★ 물리 방어력 캡 돌파 공식 (분모 50.0)
                // 공식: 남은 대미지 * (1 - (초과분 / (초과분 + 50)))
                float reductionFactor = excessArmor / (excessArmor + 50.0f);

                finalDamage = originalDamage * (1.0f - reductionFactor);
            }
        }

        // 대미지가 변경되었을 때만 적용 (불필요한 세팅 방지)
        if (finalDamage != originalDamage) {
            event.setAmount(finalDamage);
        }
    }
}
