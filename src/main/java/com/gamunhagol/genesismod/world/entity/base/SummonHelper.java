package com.gamunhagol.genesismod.world.entity.base;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SummonHelper {
    private static final UUID ARCANE_HEALTH_MOD_UUID = UUID.fromString("B2C3D4E5-F6A1-1234-5678-9ABCDEF01234");
    private static final UUID ARCANE_DAMAGE_MOD_UUID = UUID.fromString("B2C3D4E5-F6A1-1234-5678-9ABCDEF01235");

    public static InteractionResult handleInteraction(Mob mob, Player player, InteractionHand hand) {
        if (!(mob instanceof ISummonable summonable)) return InteractionResult.PASS;

        ItemStack itemstack = player.getItemInHand(hand);
        UUID ownerId = summonable.getOwnerUUID();

        if (ownerId != null && ownerId.equals(player.getUUID()) && itemstack.isEmpty()) {
            if (!player.level().isClientSide) {
                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    stats.removeSummon(mob.getUUID());
                });
                mob.discard();
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    public static void applyArcaneScaling(Mob mob, int arcaneLevel, double damageScaleRatio) {
        double hpIncrease = arcaneLevel * 0.015D;

        double damageIncrease = hpIncrease * damageScaleRatio;

        AttributeInstance maxHealth = mob.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.removeModifier(ARCANE_HEALTH_MOD_UUID);
            maxHealth.addPermanentModifier(new AttributeModifier(
                    ARCANE_HEALTH_MOD_UUID,
                    "Arcane Health Bonus",
                    hpIncrease,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
            mob.setHealth(mob.getMaxHealth());
        }

        AttributeInstance attackDamage = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.removeModifier(ARCANE_DAMAGE_MOD_UUID);
            attackDamage.addPermanentModifier(new AttributeModifier(
                    ARCANE_DAMAGE_MOD_UUID,
                    "Arcane Damage Bonus",
                    damageIncrease,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
        }

        mob.getPersistentData().putDouble("GenesisSummonDamageMultiplier", damageIncrease);
    }
}
