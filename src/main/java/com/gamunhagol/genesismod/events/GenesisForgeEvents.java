package com.gamunhagol.genesismod.events;

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
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisForgeEvents {

    private static final CollectorSpawner COLLECTOR_SPAWNER = new CollectorSpawner();

    //  플레이어에게 스탯 Capability 부착
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "stats"), new StatCapabilityProvider());
        }
    }

    //  플레이어 틱 처리 (멘탈 파워 동기화 & 파괴 효과 시간 체크)
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            // 멘탈 파워 로직
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.tick();
                if (stats.getMentalPower() < stats.getMaxMentalPower()) {
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncMentalPower(stats.getMentalPower()),
                            (ServerPlayer) player
                    );
                }
            });

            // 1초(20틱)마다 검사
            if (player.tickCount % 20 != 0) return;

            // 파괴 피해 지속 시간 체크 (로직은 CombatEvents에서 가져옴)
            if (player.getPersistentData().contains("GenesisDestructionEndTick")) {
                long endTick = player.getPersistentData().getLong("GenesisDestructionEndTick");
                long currentTick = player.level().getGameTime();

                // 시간이 다 되었으면 효과 해제
                if (currentTick >= endTick) {
                    // ★ 변경점: CombatEvents에 있는 해제 메서드 호출 (중복 제거)
                    GenesisCombatEvents.removeDestructionEffect(player);
                }
            }
        }
    }

    //  월드 틱 처리 (커스텀 스포너)
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Level.OVERWORLD) {
                COLLECTOR_SPAWNER.tick(serverLevel);
            }
        }
    }

    //  엔티티 생성 시 AI 추가 (CollectorGuard 적대 설정)
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster monster) {
            if (!(monster instanceof CollectorGuard)) {
                monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, CollectorGuard.class, true));
            }
        }
    }

    //  엔티티 틱 처리 (세트 효과: 이동 속도 증가)
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

    //  플레이어 부활/사망 처리 (성배 아이템 리필)
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player newPlayer = event.getEntity();
            // 인벤토리 전체를 순회하며 성배 리필
            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = newPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }

    //  플레이어 아이템 드롭 시 처리 (성배 아이템 리필 - 드롭된 상태에서도)
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

    // [헬퍼 메서드] 세트 효과 체크
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
}
