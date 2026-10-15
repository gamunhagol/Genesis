package com.gamunhagol.genesismod.network.client;

import com.gamunhagol.genesismod.events.client.GenesisClientRenderEvents;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientPayloadHandler {

    public static void handleMentalSync(float mentalPower) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(mentalPower);
            });
        }
    }

    public static void handleSpellSlotSync(int maxSlots, int selectedSlot, List<String> equippedSpells) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                cap.setMemoryCapacity(maxSlots);
                cap.setSelectedSlot(selectedSlot);
                cap.getEquippedSpells().clear();
                cap.getEquippedSpells().addAll(equippedSpells);
            });
        }
    }

    public static void handleStatsSync(int vigor, int mind, int endurance, int strength, int dexterity,
                                       int intelligence, int faith, int arcane, float mental, float maxMental,
                                       boolean isLevelUpUnlocked, Set<String> learnedSpells, Set<String> unlockedNodes) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setVigor(vigor); stats.setMind(mind); stats.setEndurance(endurance);
                stats.setStrength(strength); stats.setDexterity(dexterity);
                stats.setIntelligence(intelligence); stats.setFaith(faith); stats.setArcane(arcane);
                stats.setMental(mental); stats.setMaxMental(maxMental);
                stats.setLevelUpUnlocked(isLevelUpUnlocked);

                stats.getLearnedSpells().clear();
                stats.getLearnedSpells().addAll(learnedSpells);

                stats.getUnlockedNodes().clear();
                stats.getUnlockedNodes().addAll(unlockedNodes);
            });
        }
    }

    public static void handleTotemActivation(int entityId, ItemStack totemStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(entityId);
            if (entity != null) {
                if (entity == mc.player) {
                    mc.gameRenderer.displayItemActivation(totemStack);
                }
                mc.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING);
                mc.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    public static void handleSpawnEyeBeam(Vec3 targetPos) {
        GenesisClientRenderEvents.spawnBeam(targetPos);
    }

    public static void handleGrabStateSync(boolean isGrabbed, UUID entityId) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (isGrabbed) {
                player.getPersistentData().putUUID("GenesisGrabbedEntity", entityId);
            } else {
                player.getPersistentData().remove("GenesisGrabbedEntity");
            }
        }
    }

    public static void handlePulledRockSync(boolean hasRock, boolean isLarge) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            CompoundTag tag = player.getPersistentData();
            if (hasRock) {
                tag.putBoolean("GenesisHasPulledRock", true);
                tag.putBoolean("GenesisIsLargeRock", isLarge);
            } else {
                tag.remove("GenesisHasPulledRock");
                tag.remove("GenesisIsLargeRock");
            }
        }
    }
}
