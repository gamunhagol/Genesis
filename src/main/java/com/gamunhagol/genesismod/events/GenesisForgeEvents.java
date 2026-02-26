package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import com.gamunhagol.genesismod.world.item.DivineGrailItem;
import com.gamunhagol.genesismod.world.item.GenesisArmorMaterials;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.spawner.CollectorSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.item.ItemUtils.createFilledResult;

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
    public static void onLivingDeath(LivingDeathEvent event) {
        // 플레이어가 죽는 순간 실행
        if (event.getEntity() instanceof Player player) {
            // getContainerSize()는 메인 인벤토리(0~35), 갑옷(36~39), 오프핸드(40)를 모두 포함합니다.
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                // 아이템이 성배라면 리필!
                if (!stack.isEmpty() && stack.getItem() instanceof DivineGrailItem grail) {
                    grail.refill(stack);
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
                                    newStats.isLevelUpUnlocked()
                            ),
                            serverPlayer
                    );
                }
            });
        });

        if (event.isWasDeath()) {
            for (int i = 0; i < newPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = newPlayer.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof DivineGrailItem grail) {
                    grail.refill(stack);
                }
            }
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

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        InteractionHand hand = event.getHand();
        if (itemStack.is(Items.GLASS_BOTTLE)) {

            // 도달 거리 가져오기
            double reach = player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
            //  시선 레이트레이싱 설정
            Vec3 startVec = player.getEyePosition();
            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 endVec = startVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
            // 액체 감지를 포함한 클립(Clip) 수행
            BlockHitResult hitResult = level.clip(new ClipContext(
                    startVec, endVec,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.SOURCE_ONLY,
                    player
            ));

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);

                if (state.is(GenesisBlocks.BLOOD_BLOCK.get())) {
                    level.playSound(player, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    ItemStack bloodBottle = new ItemStack(GenesisItems.BLOOD_BOTTLE.get());
                    player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, bloodBottle));

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }

                else if (state.is(GenesisBlocks.HOT_SPRING_BLOCK.get())) {
                    level.playSound(player, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                    player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, waterBottle));

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }
}