package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.PatamuRoarEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class PatamuRoarSpell extends MiracleSpell {
    public PatamuRoarSpell() {
        super("patamu_roar");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 30,
                StatType.STRENGTH, 19);
    }

    @Override
    public float getMentalCost() {
        return 6.7f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.EARTH;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 2.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.5f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1500, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1500, 0));

        if (level.isClientSide) return;

        PatamuRoarEntity roar = new PatamuRoarEntity(GenesisEntities.PATAMU_ROAR.get(), level);
        roar.setPos(caster.getX(), caster.getY(), caster.getZ());
        roar.setup(caster, spellSnapshot);

        level.addFreshEntity(roar);
    }
}