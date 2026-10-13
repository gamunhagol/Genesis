package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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

public class ValaArrowEntity extends MagicEntity implements ItemSupplier {
    private static final double AOE_RADIUS = 2.5D;

    public ValaArrowEntity(EntityType<? extends ValaArrowEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 200;
    }

    public ValaArrowEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 motion) {
        super(GenesisEntities.VALA_ARROW.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.maxLifeTicks = 200;
        this.setDeltaMovement(motion);
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

            triggerImpactEffects(result.getLocation(), target);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            triggerImpactEffects(result.getLocation(), null);
            this.discard();
        }
    }

    private void triggerImpactEffects(Vec3 hitPos, Entity directTarget) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        Entity owner = this.getOwner();

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
        if (bolt != null) {
            bolt.moveTo(hitPos.x, hitPos.y, hitPos.z);
            if (owner instanceof ServerPlayer serverPlayer) {
                bolt.setCause(serverPlayer);
            }
            serverLevel.addFreshEntity(bolt);
        }

        AABB strikeBox = new AABB(
                hitPos.x - AOE_RADIUS, hitPos.y - 1.0D, hitPos.z - AOE_RADIUS,
                hitPos.x + AOE_RADIUS, hitPos.y + 2.0D, hitPos.z + AOE_RADIUS
        );

        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                strikeBox,
                e -> e.isAlive() && e != owner && e != directTarget
        );

        double radiusSqr = AOE_RADIUS * AOE_RADIUS;
        for (LivingEntity target : targets) {
            if (target.distanceToSqr(hitPos) <= radiusSqr) {
                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
                target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
            }
        }
    }
}
