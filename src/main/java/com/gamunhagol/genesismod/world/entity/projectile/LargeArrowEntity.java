package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class LargeArrowEntity extends AbstractArrow {
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
    protected ItemStack getPickupItem() {
        return new ItemStack(GenesisItems.LARGE_ARROW.get());
    }
}
