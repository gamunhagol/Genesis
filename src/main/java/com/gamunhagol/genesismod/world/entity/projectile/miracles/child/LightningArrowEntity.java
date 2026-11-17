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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LightningArrowEntity extends MagicEntity implements ItemSupplier {

    public LightningArrowEntity(EntityType<? extends LightningArrowEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 600;
    }

    public LightningArrowEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 motion) {
        super(GenesisEntities.LIGHTNING_ARROW.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.maxLifeTicks = 600;
        this.setDeltaMovement(motion);
        this.hasImpulse = true;
        this.updateRotation();

        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
    }

    public void releaseArrow(DamageSnapshot snapshot, Vec3 motion) {
        this.damageSnapshot = snapshot;
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        this.setDeltaMovement(motion);
        this.hasImpulse = true;
        this.updateRotation();
        this.setCharging(false);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isHit()) return;

        if (this.isCharging()) {
            if (this.getOwner() instanceof LivingEntity owner) {
                this.setYRot(-owner.getYRot());
                this.setXRot(-owner.getXRot());
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
            return;
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        if (this.isRemoved() || this.isHit()) return;

        Vec3 motion = this.getDeltaMovement();
        this.updateRotation();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.isHit() || this.isCharging()) return;
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            Entity owner = this.getOwner();

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
            target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);

            this.triggerHit(result.getLocation());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.isHit() || this.isCharging()) return;
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.triggerHit(result.getLocation());
        }
    }
}
