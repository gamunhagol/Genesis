package com.gamunhagol.genesismod.world.entity.etc.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.AbstractRootEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PoisonRootEntity extends AbstractRootEntity {

    public PoisonRootEntity(EntityType<? extends PoisonRootEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 400;
    }

    public PoisonRootEntity(Level level, @Nullable LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.POISON_ROOT.get(), level, owner, snapshot);
        this.maxLifeTicks = 400;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createRootAttributes(10.0F);
    }

    @Override
    protected void onEntityContact(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1, false, true, true));
    }
}
