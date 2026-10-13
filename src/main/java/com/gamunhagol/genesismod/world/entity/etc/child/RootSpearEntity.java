package com.gamunhagol.genesismod.world.entity.etc.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.AbstractRootEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RootSpearEntity extends AbstractRootEntity {
    private final Map<UUID, Long> attackCooldowns = new HashMap<>();

    public RootSpearEntity(EntityType<? extends RootSpearEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 160;
    }

    public RootSpearEntity(Level level, @Nullable LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.ROOT_SPEAR.get(), level, owner, snapshot);
        this.maxLifeTicks = 160;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createRootAttributes(20.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && this.random.nextFloat() < 0.35F) {
            this.level().addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.4D,
                    this.getY() + (this.random.nextDouble() * this.getBbHeight()),
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.4D,
                    0.0D, 0.05D, 0.0D
            );
        }
    }

    @Override
    protected void onEntityContact(LivingEntity target) {
        long currentTime = this.level().getGameTime();
        long lastHitTime = this.attackCooldowns.getOrDefault(target.getUUID(), 0L);

        if (currentTime - lastHitTime >= 20L) {
            this.attackCooldowns.put(target.getUUID(), currentTime);

            LivingEntity owner = getOwnerEntity();
            if (this.damageSnapshot != null && !this.damageSnapshot.isEmpty() && owner != null) {
                GenesisDamageCalculator.applySnapshotDamage(target, owner, this.damageSnapshot);
            } else {
                target.hurt(this.damageSources().mobAttack(owner != null ? owner : this), 8.0F);
            }
        }
    }
}
