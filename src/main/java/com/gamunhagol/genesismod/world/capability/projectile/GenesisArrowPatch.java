package com.gamunhagol.genesismod.world.capability.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisArrowPatch extends ArrowPatch {

    private static final float BASE_IMPACT = 8.95F;
    private static final float EMPOWERED_IMPACT = 22.1F;

    public float getImpact() {
        if (this.original instanceof LargeArrowEntity largeArrow && largeArrow.isEmpowered()) {
            return EMPOWERED_IMPACT;
        }
        return this.impact > 0.0F ? this.impact : BASE_IMPACT;
    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        EpicFightDamageSource source = super.createEpicFightDamageSource();

        boolean empowered = false;
        if (this.original instanceof AbstractArrow && this.original instanceof LargeArrowEntity largeArrow) {
            empowered = largeArrow.isEmpowered();
        }

        source.setBaseImpact(this.getImpact());
        source.setStunType(empowered ? StunType.KNOCKDOWN : StunType.LONG);

        return source;
    }
}
