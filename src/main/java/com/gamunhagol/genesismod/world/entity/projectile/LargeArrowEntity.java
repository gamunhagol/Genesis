package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;

public class LargeArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> EMPOWERED = SynchedEntityData.defineId(LargeArrowEntity.class, EntityDataSerializers.BOOLEAN);

    public LargeArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(5.0D);
    }

    public LargeArrowEntity(Level level, LivingEntity shooter) {
        // 변수명을 LARGE_ARROW_ENTITY로 수정했다고 가정
        super(GenesisEntities.LARGE_ARROW.get(), shooter, level);
    }

    @Override
    public void tick() {
        super.tick();

        // 화살이 공중에 있고, 중력의 영향을 받는 상태일 때만 추가 중력 적용
        if (!this.onGround() && !this.isNoGravity()) {
            // 바닐라 기본 중력은 0.05입니다.
            // 여기에 0.03을 더하면 총 0.08의 중력이 되어 확실히 묵직하게 떨어집니다.
            double extraGravity = 0.03D;

            net.minecraft.world.phys.Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y - extraGravity, motion.z);
        }
    }

    // 추가 권장 생성자 (좌표 기반 소환용)
    public LargeArrowEntity(Level level, double x, double y, double z) {
        super(GenesisEntities.LARGE_ARROW.get(), x, y, z, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // 에픽 파이트 연동 시 이 부분에서 커스텀 DamageSource를 사용하게 됩니다.
        // 지금은 단계적 확인 중이니 기본 로직을 먼저 태우세요.
        super.onHitEntity(result);

        /* 추후 에픽 파이트 로직 예시:
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity target) {
            // 여기서 Impact 수치를 포함한 대미지를 입히는 코드를 작성합니다.
        }
        */
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EMPOWERED, false);
    }

    public void setEmpowered(boolean value) {
        this.entityData.set(EMPOWERED, value);
    }

    public boolean isEmpowered() {
        return this.entityData.get(EMPOWERED);
    }
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide && this.isEmpowered()) {
            Vec3 hitPos = result.getLocation();
            LivingEntity owner = (LivingEntity) this.getOwner();

            // [수정 1] 높이 판정 보정
            // hitPos는 블록 표면이라 공중 판정이 날 수 있습니다.
            // 0.2 정도 아래를 타격점으로 잡아 블록 내부를 긁도록 수정했습니다.
            Vec3 slamPos = new Vec3(hitPos.x, hitPos.y - 0.2, hitPos.z);

            // [수정 2] 지면 파괴 로직 호출 (반지름 5.0으로 상향)
            // LevelUtil 내부에서 이미 광역 피해(true)를 입히므로 코드가 간결해집니다.
            LevelUtil.circleSlamFracture(owner, this.level(), slamPos, 5.0D, false, false, true);

            // [수정 3] 직접 피해 로직 (필요한 경우에만 추가)
            // LevelUtil의 피해량이 부족하거나 커스텀 경직을 주고 싶을 때만 아래를 사용하세요.
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(slamPos.add(-4, -1, -4), slamPos.add(4, 2, 4)));

            EpicFightDamageSource shockwaveSource = EpicFightDamageSources.shockwave(owner);
            shockwaveSource.setStunType(StunType.KNOCKDOWN);
            shockwaveSource.setBaseImpact(15.0F);

            for (LivingEntity target : targets) {
                if (target != owner) {
                    target.hurt(shockwaveSource, 12.0F); // 12.0의 충격파 피해
                }
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(GenesisItems.LARGE_ARROW.get());
    }
}
