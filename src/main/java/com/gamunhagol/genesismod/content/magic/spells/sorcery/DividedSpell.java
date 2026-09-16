package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.DividedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class DividedSpell extends MagicSpell {
    public DividedSpell() {
        super("divided");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 30);
    }

    @Override
    public float getMentalCost() {
        return 12.5f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float finalMagic = 18.8f + (catalyst.magic() * 1.2f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    protected DamageSnapshot calculateChildSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float childMagic = 4.5f + (catalyst.magic() * 0.7f);
        return new DamageSnapshot(0, childMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        DamageSnapshot catalyst = WeaponRequirementHelper.calculateTotalDamage(caster, caster.getMainHandItem(), 0f);
        DamageSnapshot childSnapshot = calculateChildSnapshot(caster, catalyst);

        Vec3 look = caster.getLookAngle();
        DividedEntity divided = new DividedEntity(level, caster, spellSnapshot, childSnapshot);

        divided.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        divided.setDeltaMovement(look.scale(0.32D));

        divided.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(divided);
    }
}