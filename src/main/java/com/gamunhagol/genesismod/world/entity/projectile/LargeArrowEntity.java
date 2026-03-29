package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LargeArrowEntity extends AbstractArrow {
    public LargeArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public LargeArrowEntity(Level level, LivingEntity shooter) {
        // 변수명을 LARGE_ARROW_ENTITY로 수정했다고 가정
        super(GenesisEntities.LARGE_ARROW.get(), shooter, level);
    }

    // 추가 권장 생성자 (좌표 기반 소환용)
    public LargeArrowEntity(Level level, double x, double y, double z) {
        super(GenesisEntities.LARGE_ARROW.get(), x, y, z, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(GenesisItems.LARGE_ARROW.get());
    }
}
