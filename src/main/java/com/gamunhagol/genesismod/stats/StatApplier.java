package com.gamunhagol.genesismod.stats;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;


import java.util.UUID;

public class StatApplier {
    public static final UUID VIGOR_MOD_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-1111-111111111111");
    public static final UUID ENDURANCE_MAX_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222222");
    public static final UUID ENDURANCE_REGEN_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222223");
    public static final UUID ENDURANCE_WEIGHT_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222224");
    public static final UUID LUCK_MOD_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-3333-333333333333");

    public static void applyAll(Player player, StatCapability stats) {
        applyVigor(player, stats.getVigor());
        applyEndurance(player, stats.getEndurance());
        applyArcane(player, stats.getArcane());
        stats.updateMaxMental();

        sync(player, GenesisAttributes.VIGOR.get(), stats.getVigor());
        sync(player, GenesisAttributes.MIND.get(), stats.getMind());
        sync(player, GenesisAttributes.ENDURANCE.get(), stats.getEndurance());
        sync(player, GenesisAttributes.STRENGTH.get(), stats.getStrength());
        sync(player, GenesisAttributes.DEXTERITY.get(), stats.getDexterity());
        sync(player, GenesisAttributes.INTELLIGENCE.get(), stats.getIntelligence());
        sync(player, GenesisAttributes.FAITH.get(), stats.getFaith());
        sync(player, GenesisAttributes.ARCANE.get(), stats.getArcane());
    }

    private static void sync(Player player, Attribute attribute, int value) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst != null) {
            inst.setBaseValue(value);
        }
    }

    public static void applyVigor(Player player, int level) {
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            attr.removeModifier(VIGOR_MOD_UUID);

            float totalHealthGoal;
            if (level <= 10) {
                totalHealthGoal = 2.0f + (level - 1) * 2.0f;
            } else if (level <= 50) {
                totalHealthGoal = 20.0f + (level - 10) * 2.0f;
            } else if (level < 99) {
                totalHealthGoal = 100.0f + (level - 50) * 1.0f;
            } else {
                totalHealthGoal = 150.0f;
            }

            float modifierValue = totalHealthGoal - 20.0f;
            attr.addPermanentModifier(new AttributeModifier(VIGOR_MOD_UUID, "Genesis Vigor", modifierValue, AttributeModifier.Operation.ADDITION));

            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static void applyEndurance(Player player, int level) {
        AttributeInstance maxStam = player.getAttribute(EpicFightAttributes.MAX_STAMINA.get());
        if (maxStam != null) {
            maxStam.removeModifier(ENDURANCE_MAX_UUID);

            float staminaGoal;
            if (level <= 10) {
                staminaGoal = 2.0f + (level - 1) * 1.444f;
            } else if (level <= 20) {
                staminaGoal = 15.0f + (level - 10) * 1.5f;
            } else if (level < 99) {
                staminaGoal = 30.0f + (level - 20) * 1.0f;
            } else {
                staminaGoal = 110.0f;
            }
            maxStam.addPermanentModifier(new AttributeModifier(ENDURANCE_MAX_UUID, "Genesis Endurance Max", staminaGoal - 15.0f, AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance regenStam = player.getAttribute(EpicFightAttributes.STAMINA_REGEN.get());
        if (regenStam != null) {
            regenStam.removeModifier(ENDURANCE_REGEN_UUID);
            regenStam.addPermanentModifier(new AttributeModifier(ENDURANCE_REGEN_UUID, "Genesis Endurance Regen", level * 0.01f, AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance weight = player.getAttribute(EpicFightAttributes.WEIGHT.get());
        if (weight != null) {
            weight.removeModifier(ENDURANCE_WEIGHT_UUID);
            weight.addPermanentModifier(new AttributeModifier(ENDURANCE_WEIGHT_UUID, "Genesis Endurance Weight Mitigation", level * -0.5f, AttributeModifier.Operation.ADDITION));
        }
    }


    public static void applyArcane(Player player, int level) {
        AttributeInstance attr = player.getAttribute(Attributes.LUCK);
        if (attr != null) {
            attr.removeModifier(LUCK_MOD_UUID);
            attr.addPermanentModifier(new AttributeModifier(LUCK_MOD_UUID, "Genesis Arcane", level * 0.05f, AttributeModifier.Operation.ADDITION));
        }
    }

    public static float calculateScaling(int level) {
        if (level <= 20) {
            return level * 0.0175f;
        }
        else if (level <= 60) {
            return 0.35f + (level - 20) * 0.01f;
        }
        else if (level <= 80) {
            return 0.75f + (level - 60) * 0.0075f;
        }
        else {
            return 0.90f + (Math.min(level, 99) - 80) * 0.005f;
        }
    }
}