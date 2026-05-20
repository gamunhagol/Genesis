package com.gamunhagol.genesismod.content.magic.spells;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends MagicSpell {
    public FireballSpell() { super("fireball"); }

    @Override
    public int getCastTime() { return 20; }
    @Override
    public int getRequiredStatLevel() { return 10; }
    @Override
    public float getMentalCost() { return 2.0f; }
    @Override
    public int getMemoryCost() {return 1;} // 기본 마법은 1칸, 강력한 궁극기는 2~3칸으로 설정

    @Override
    protected DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalyst) {
        float basePower = 2.0f;

        // 촉매 마력 효율 계수 설정 (0.4 = 촉매 마력의 40%만 데미지에 반영)
        // 촉매 magic이 10.0이라면 4.0의 추가 데미지만 발생함.
        float magicEfficiency = 0.4f;
        float fireEfficiency = 0.2f;

        float finalFire = basePower + (catalyst.magic() * magicEfficiency) + (catalyst.fire() * fireEfficiency);

        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }
    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        SmallFireball fireball = new SmallFireball(level, caster, look.x, look.y, look.z);
        fireball.setPos(caster.getX(), caster.getEyeY(), caster.getZ());

        // 계산된 스냅샷을 투사체에 쏙 주입
        fireball.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(fireball);
    }
}