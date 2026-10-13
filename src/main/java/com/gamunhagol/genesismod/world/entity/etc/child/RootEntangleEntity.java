package com.gamunhagol.genesismod.world.entity.etc.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.AbstractRootEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RootEntangleEntity extends AbstractRootEntity {

    public RootEntangleEntity(EntityType<? extends RootEntangleEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 400;
    }

    public RootEntangleEntity(Level level, @Nullable LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.ROOT_ENTANGLE.get(), level, owner, snapshot);
        this.maxLifeTicks = 400;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createRootAttributes(10.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && this.random.nextFloat() < 0.2F) {
            this.level().addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                    this.getY() + 0.1D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                    0.0D, 0.02D, 0.0D
            );
        }
    }

    @Override
    protected void onEntityContact(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4, false, false, true));
    }
}
