package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PatamuWrathEntity extends Entity implements ItemSupplier {
    private double slamRadius = 7.0D;
    private double hitRadius = 2.0D;

    private UUID ownerUUID = null;
    private DamageSnapshot snapshot = DamageSnapshot.EMPTY;

    public PatamuWrathEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setup(LivingEntity owner, DamageSnapshot snapshot) {
        this.ownerUUID = owner.getUUID();
        this.snapshot = snapshot;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x, motion.y - 0.12D, motion.z);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            if (this.onGround()) {
                detonate(serverLevel);
                this.discard();
            }
        }
    }

    private void detonate(ServerLevel level) {
        LivingEntity owner = getOwner(level);
        if (owner == null) return;

        Vec3 slamPos = new Vec3(this.getX(), this.getY() - 0.2D, this.getZ());

        LevelUtil.circleSlamFracture(owner, level, slamPos, this.slamRadius, false, false, false);

        float basePhysical = this.snapshot.physical() > 0 ? this.snapshot.physical() : 24.0F;
        EpicFightDamageSource shockwaveSource = EpicFightDamageSources.shockwave(owner); //[cite: 1]

        AABB directHitBox = new AABB(
                slamPos.x - this.hitRadius, slamPos.y - 1.0D, slamPos.z - this.hitRadius,
                slamPos.x + this.hitRadius, slamPos.y + 2.5D, slamPos.z + this.hitRadius
        );
        List<LivingEntity> directTargets = level.getEntitiesOfClass(LivingEntity.class, directHitBox, e -> e.isAlive() && e != owner);
        for (LivingEntity target : directTargets) {
            target.hurt(shockwaveSource, basePhysical);
        }


        float shockwaveDamage = basePhysical * 0.4F;

        AABB shockwaveBox = new AABB(
                slamPos.x - this.slamRadius, slamPos.y - 1.5D, slamPos.z - this.slamRadius,
                slamPos.x + this.slamRadius, slamPos.y + 2.5D, slamPos.z + this.slamRadius
        );
        List<LivingEntity> shockwaveTargets = level.getEntitiesOfClass(LivingEntity.class, shockwaveBox, e -> e.isAlive() && e != owner);
        for (LivingEntity target : shockwaveTargets) {
            if (!directTargets.contains(target)) {
                target.hurt(shockwaveSource, shockwaveDamage);
            }
        }
    }

    @Nullable
    private LivingEntity getOwner(ServerLevel level) {
        if (this.ownerUUID != null && level.getEntity(this.ownerUUID) instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }
    @Override protected MovementEmission getMovementEmission() { return MovementEmission.NONE; }
    @Override protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}