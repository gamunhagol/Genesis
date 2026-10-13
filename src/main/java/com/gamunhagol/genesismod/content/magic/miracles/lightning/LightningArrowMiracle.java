package com.gamunhagol.genesismod.content.magic.miracles.lightning;

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

public class LightningArrowMiracle extends MiracleSpell {
    public static final int DURATION_TICKS = 600;

    public LightningArrowMiracle() {
        super("lightning_arrow");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 15,
                StatType.STRENGTH, 12);
    }

    @Override
    public float getMentalCost() {
        return 8.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.LIGHTNING;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!super.canCast(caster)) return false;

        if (caster instanceof Player player) {
            return player.getInventory().getFreeSlot() != -1;
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

        ItemStack summonedBow = new ItemStack(Items.BOW);
        long expireTick = level.getGameTime() + DURATION_TICKS;

        summonedBow.getOrCreateTag().putBoolean(FlameHammerMiracle.TAG_SUMMONED_WEAPON, true);
        summonedBow.getOrCreateTag().putLong(FlameHammerMiracle.NBT_KEY_EXPIRE_TICK, expireTick);

        if (player.getMainHandItem().isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, summonedBow);
        } else {
            int freeSlot = player.getInventory().getFreeSlot();
            if (freeSlot != -1) {
                player.getInventory().setItem(freeSlot, summonedBow);
            }
        }
    }
}
