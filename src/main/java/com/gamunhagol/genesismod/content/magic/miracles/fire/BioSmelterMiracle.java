package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class BioSmelterMiracle extends MiracleSpell {
    public BioSmelterMiracle() {
        super("bio_smelter");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 18);
    }

    @Override
    public float getMentalCost() {
        return 7.9f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        caster.hurt(caster.damageSources().inFire(), 10.0F);

        if (caster instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                repairStack(stack, 100);
            }
        } else {
            for (ItemStack stack : caster.getAllSlots()) {
                repairStack(stack, 100);
            }
        }

        level.playSound(
                null,
                caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ANVIL_USE,
                SoundSource.PLAYERS,
                0.8F,
                1.0F
        );
    }

    private void repairStack(ItemStack stack, int amount) {
        if (!stack.isEmpty() && stack.isDamageableItem() && stack.getDamageValue() > 0) {
            int currentDamage = stack.getDamageValue();
            int newDamage = Math.max(0, currentDamage - amount);
            stack.setDamageValue(newDamage);
        }
    }
}