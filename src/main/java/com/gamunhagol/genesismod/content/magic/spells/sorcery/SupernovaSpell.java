package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.SupernovaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class SupernovaSpell extends MagicSpell {
    public SupernovaSpell() {
        super("supernova");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.INTELLIGENCE, 50,
                StatType.FAITH, 30
        );
    }

    @Override
    public float getMentalCost() {
        return 49.0f;
    }

    @Override
    public int getMemoryCost() {
        return 3;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float physical = 10.0f;
        float magic = 10.0f + (catalyst.magic() * 0.45f);
        float fire = 10.0f + (catalyst.fire() * 0.35f);
        float lightning = 10.0f + (catalyst.lightning() * 0.25f);
        float frost = 10.0f + (catalyst.frost() * 0.25f);
        float holy = 10.0f + (catalyst.holy() * 0.25f);
        float destruction = 10.0f;

        return new DamageSnapshot(physical, magic, fire, lightning, frost, holy, destruction);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.getEyePosition().add(look.scale(0.8D));

        SupernovaEntity supernova = new SupernovaEntity(level, caster, spellSnapshot, look);
        supernova.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        supernova.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(supernova);
    }
}