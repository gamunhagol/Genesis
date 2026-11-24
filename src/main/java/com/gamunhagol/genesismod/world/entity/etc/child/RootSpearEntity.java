package com.gamunhagol.genesismod.world.entity.etc.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.AbstractRootEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.damagesource.StunType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public int getRiseDuration() {
        return 5;
    }

    @Override
    public float getModelHeight() {
        return 2.0F;
    }

    @Override
    public float getMaxExposureRatio() {
        return 0.8F;
    }

    @Override
    protected void checkEntityContacts() {
        float progress = Mth.clamp((float) this.tickCount / (float) getRiseDuration(), 0.0F, 1.0F);
        float currentLength = getModelHeight() * getMaxExposureRatio() * progress;
        if (currentLength <= 0.05F) return;

        float yawRad = getRootYaw() * Mth.DEG_TO_RAD;
        float tiltRad = getRootTilt() * Mth.DEG_TO_RAD;

        double dirX = Mth.sin(tiltRad) * Mth.sin(yawRad);
        double dirY = Mth.cos(tiltRad);
        double dirZ = Mth.sin(tiltRad) * Mth.cos(yawRad);

        Set<LivingEntity> hitTargets = new HashSet<>();
        int segments = 4;
        double radius = 0.35D;

        for (int i = 1; i <= segments; i++) {
            double dist = currentLength * ((double) i / (double) segments);
            double px = this.getX() + dirX * dist;
            double py = this.getY() + dirY * dist;
            double pz = this.getZ() + dirZ * dist;

            AABB segmentBox = new AABB(
                    px - radius, py - radius, pz - radius,
                    px + radius, py + radius, pz + radius
            );

            List<LivingEntity> found = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    segmentBox,
                    this::isValidTarget
            );
            hitTargets.addAll(found);
        }

        for (LivingEntity target : hitTargets) {
            onEntityContact(target);
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
                GenesisDamageCalculator.applySnapshotDamage(target, owner, this.damageSnapshot, 1.75F, StunType.LONG);
            } else {
                target.hurt(this.damageSources().mobAttack(owner != null ? owner : this), 8.0F);
            }
        }
    }
}
