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

public class LightningStakeEntity extends MagicEntity implements ItemSupplier {
    private static final double STAKE_SPEED = 2.5D;
    private static final double AOE_RADIUS = 3.5D;

    public LightningStakeEntity(EntityType<? extends LightningStakeEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 100;
    }

    public LightningStakeEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 shootDir) {
        super(GenesisEntities.LIGHTNING_STAKE.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.maxLifeTicks = 100;
        this.setDeltaMovement(shootDir.normalize().scale(STAKE_SPEED));
        this.hasImpulse = true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        if (this.isRemoved()) return;

        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            Entity owner = this.getOwner();

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
            target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);

            applyAreaDamage(result.getLocation(), target);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            applyAreaDamage(result.getLocation(), null);
            this.discard();
        }
    }

    private void applyAreaDamage(Vec3 center, Entity directHitTarget) {
        Entity owner = this.getOwner();
        AABB area = new AABB(
                center.x - AOE_RADIUS, center.y - 1.0D, center.z - AOE_RADIUS,
                center.x + AOE_RADIUS, center.y + 2.0D, center.z + AOE_RADIUS
        );

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive() && e != owner && e != directHitTarget
        );

        double radiusSqr = AOE_RADIUS * AOE_RADIUS;
        for (LivingEntity target : targets) {
            if (target.distanceToSqr(center) <= radiusSqr) {
                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
                target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
            }
        }
    }
}