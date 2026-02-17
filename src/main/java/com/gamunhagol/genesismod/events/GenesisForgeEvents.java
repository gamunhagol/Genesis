package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatApplier;
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

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.tick();
                if (stats.isDirty()) {
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncStats(
                                    stats.getVigor(),
                                    stats.getMind(),
                                    stats.getEndurance(),
                                    stats.getStrength(),
                                    stats.getDexterity(),
                                    stats.getIntelligence(),
                                    stats.getFaith(),
                                    stats.getArcane(),
                                    stats.getMental(),
                                    stats.getMaxMental(),
                                    stats.isLevelUpUnlocked() // [수정됨] 맨 뒤에 해금 여부 추가!
                            ),
                            (ServerPlayer) player
                    );
                    stats.setDirty(false);
                }
            });

            // 파괴 효과 체크 (기존 유지)
            if (player.tickCount % 20 == 0) {
                if (player.getPersistentData().contains("GenesisDestructionEndTick")) {
                    long endTick = player.getPersistentData().getLong("GenesisDestructionEndTick");
                    if (player.level().getGameTime() >= endTick) {
                        GenesisCombatEvents.removeDestructionEffect(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        original.reviveCaps();

        original.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(oldStats -> {
            newPlayer.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(newStats -> {
                newStats.copyFrom(oldStats);
                StatApplier.applyAll(newPlayer, newStats);

                if (newPlayer instanceof ServerPlayer serverPlayer) {
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncStats(
                                    newStats.getVigor(), newStats.getMind(), newStats.getEndurance(),
                                    newStats.getStrength(), newStats.getDexterity(), newStats.getIntelligence(),
                                    newStats.getFaith(), newStats.getArcane(),
                                    newStats.getMental(), newStats.getMaxMental(),
                                    newStats.isLevelUpUnlocked() // [수정됨] 여기도 맨 뒤에 추가!
                            ),
                            serverPlayer
                    );
                }
            });
        });

        if (event.isWasDeath()) {
            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = newPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof com.gamunhagol.genesismod.world.item.DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
        }
    }

    // ... (헬퍼 메서드 유지) ...
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
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                // 접속하자마자 현재 상태를 패킷으로 보냄
                GenesisNetwork.sendToPlayer(
                        new PacketSyncStats(
                                stats.getVigor(), stats.getMind(), stats.getEndurance(),
                                stats.getStrength(), stats.getDexterity(), stats.getIntelligence(),
                                stats.getFaith(), stats.getArcane(),
                                stats.getMental(), stats.getMaxMental(),
                                stats.isLevelUpUnlocked() // 이 정보가 클라이언트로 가야 버튼이 생깁니다.
                        ),
                        player
                );
            });
        }
    }
}