package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EmberEntity extends MagicEntity implements ItemSupplier {

    protected float explosionPower = 1.2F; // 낮은 강도의 폭발력

    public EmberEntity(EntityType<? extends EmberEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 4;
    }

    public EmberEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.EMBER.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.maxLifeTicks = 4;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.FIRE_CHARGE);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }

            checkProximityHit();

            if (this.isRemoved()) return;

            if (this.lifeTicks >= this.maxLifeTicks) {
                detonate();
                this.discard();
                return;
            }
        }

        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    private void checkProximityHit() {
        AABB hitBox = this.getBoundingBox().inflate(0.3D);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                hitBox,
                e -> e.isAlive() && e != owner
        );

        if (!targets.isEmpty()) {
            detonate();
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            detonate();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            detonate();
            this.discard();
        }
    }

    private void detonate() {
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.NONE
        );
    }
}