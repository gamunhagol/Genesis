package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.events.common.GenesisCombatEvents;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketActivateCustomTotem;
import com.gamunhagol.genesismod.network.PacketSyncSpellSlot;
import com.gamunhagol.genesismod.network.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.util.GenesisTags;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import com.gamunhagol.genesismod.world.item.tool.DivineGrailItem;
import com.gamunhagol.genesismod.world.item.GenesisArmorMaterials;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.spawner.CollectorSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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


@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisForgeEvents {

    private static final CollectorSpawner COLLECTOR_SPAWNER = new CollectorSpawner();

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "stats"), new StatCapabilityProvider());
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "spell_slot_data"),
                    new SpellSlotProvider());
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                    monster, LivingEntity.class, 10, true, false,
                    (target) -> target.getType().is(GenesisTags.EntityTypes.FACTION_MOBS)
            ));
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.level().isClientSide && entity.tickCount % 20 == 0) {
            if (hasFullPaddedChainSet(entity)) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0, false, false, true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.tick();
                if (stats.isDirty()) {
                    GenesisNetwork.sendToPlayer(new PacketSyncStats(stats), (ServerPlayer) player);
                    stats.setDirty(false);
                }
            });

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
        LivingEntity entity = event.getEntity();

        ItemStack mainHandItem = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = entity.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack activeTotem = ItemStack.EMPTY;

        if (mainHandItem.is(GenesisItems.AMETHYST_HUMAN_STATUE.get())) {
            activeTotem = mainHandItem;
        } else if (offHandItem.is(GenesisItems.AMETHYST_HUMAN_STATUE.get())) {
            activeTotem = offHandItem;
        }

        if (!activeTotem.isEmpty()) {
            event.setCanceled(true);

            entity.setHealth(1.0F);

            entity.removeAllEffects();

            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

            GenesisNetwork.sendToTrackingAndSelf(new PacketActivateCustomTotem(entity.getId(), activeTotem.copy()), entity);

            activeTotem.shrink(1);

            return;
        }

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

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
                    GenesisNetwork.sendToPlayer(new PacketSyncStats(newStats), serverPlayer);
                }
            });
        });

        if (event.isWasDeath()) {
            original.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(oldStore -> {
                newPlayer.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);

                    if (newPlayer instanceof ServerPlayer serverPlayer) {
                        GenesisNetwork.sendToPlayer(
                                new com.gamunhagol.genesismod.network.PacketSyncSpellSlot(
                                        newStore.getMemoryCapacity(),
                                        newStore.getSelectedSlot(),
                                        newStore.getEquippedSpells()
                                ),
                                serverPlayer
                        );
                    }
                    if (newPlayer.getPersistentData().contains("GenesisStatueHealReadyTick")) {
                        newPlayer.getPersistentData().remove("GenesisStatueHealReadyTick");
                    }
                });
            });
        }

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
                GenesisNetwork.sendToPlayer(new PacketSyncStats(stats), player);
            });
            player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                GenesisNetwork.sendToPlayer(
                        new PacketSyncSpellSlot(cap.getMemoryCapacity(), cap.getSelectedSlot(), cap.getEquippedSpells()),
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

            double reach = player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
            Vec3 startVec = player.getEyePosition();
            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 endVec = startVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
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
                
            }
        }
    }
}