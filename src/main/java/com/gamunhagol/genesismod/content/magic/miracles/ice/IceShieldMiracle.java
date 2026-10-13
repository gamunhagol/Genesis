package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.content.magic.miracles.fire.FlameHammerMiracle;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Map;

public class IceShieldMiracle extends MiracleSpell {
    public static final int DURATION_TICKS = 600;

    public IceShieldMiracle() {
        super("ice_shield");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 15);
    }

    @Override
    public float getMentalCost() {
        return 5.4f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!super.canCast(caster)) return false;

        if (caster instanceof Player player) {
            return player.getOffhandItem().isEmpty() || player.getInventory().getFreeSlot() != -1;
        }
        return true;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide || !(caster instanceof Player player)) return;

        ItemStack summonedShield = new ItemStack(Items.SHIELD);
        long expireTick = level.getGameTime() + DURATION_TICKS;

        summonedShield.getOrCreateTag().putBoolean(FlameHammerMiracle.TAG_SUMMONED_WEAPON, true);
        summonedShield.getOrCreateTag().putLong(FlameHammerMiracle.NBT_KEY_EXPIRE_TICK, expireTick);

        if (player.getOffhandItem().isEmpty()) {
            player.setItemInHand(InteractionHand.OFF_HAND, summonedShield);
        } else {
            int freeSlot = player.getInventory().getFreeSlot();
            if (freeSlot != -1) {
                player.getInventory().setItem(freeSlot, summonedShield);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}