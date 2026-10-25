package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.nbt.CompoundTag;
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
    private boolean inGround = false;
    private int inGroundTime = 0;
    private static final int MAX_IN_GROUND_TICKS = 100;

    public LightningArrowEntity(EntityType<? extends LightningArrowEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false);
        this.maxLifeTicks = 200;
    }

    public LightningArrowEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 motion) {
        super(GenesisEntities.LIGHTNING_ARROW.get(), level, owner, snapshot);
        this.setNoGravity(false);
        this.maxLifeTicks = 200;
        this.setDeltaMovement(motion);
        this.hasImpulse = true;

        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.inGround) {
            this.inGroundTime++;
            if (this.inGroundTime >= MAX_IN_GROUND_TICKS) {
                this.discard();
            }
            return;
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        if (this.isRemoved()) return;

        Vec3 motion = this.getDeltaMovement();
        double nextX = this.getX() + motion.x;
        double nextY = this.getY() + motion.y;
        double nextZ = this.getZ() + motion.z;

        this.updateRotation();

        if (!this.isNoGravity()) {
            Vec3 gravity = new Vec3(0.0D, -0.05D, 0.0D); // 화살 표준 중력
            this.setDeltaMovement(motion.add(gravity).scale(0.99D));
        }

        this.setPos(nextX, nextY, nextZ);
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

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.inGround = true;
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoGravity(true);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("InGround", this.inGround);
        tag.putInt("InGroundTime", this.inGroundTime);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inGround = tag.getBoolean("InGround");
        this.inGroundTime = tag.getInt("InGroundTime");
    }
}
