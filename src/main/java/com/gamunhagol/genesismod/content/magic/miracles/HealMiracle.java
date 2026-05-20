package com.gamunhagol.genesismod.content.magic.miracles;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HealMiracle extends MiracleSpell {
    public HealMiracle() { super("little_heal"); }

    @Override
    public int getCastTime() { return 45; }
    @Override
    public int getRequiredStatLevel() { return 10; }
    @Override
    public float getMentalCost() { return 3.0f; }
    @Override
    public int getMemoryCost() {return 1;} // 기본 마법은 1칸, 강력한 궁극기는 2~3칸으로 설정

    @Override
    protected DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalyst) {
        // 기본 회복량 4.0 + 촉매의 신성력(Holy)의 100%
        float healAmount = 4.0f + catalyst.holy();

        float efficiency = 0.3f;
        float finalHeal = healAmount + (catalyst.holy() * efficiency);

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        // holy 칸에 담아둔 수치를 회복량으로 사용
        caster.heal(spellSnapshot.holy());
    }
}