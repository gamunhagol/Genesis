package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class PebbleEntity extends StoneProjectile {
    public PebbleEntity(EntityType<? extends PebbleEntity> type, Level level) {
        super(type, level);
        this.gravity = 0.025D;
    }

    public PebbleEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.PEBBLE.get(), level, owner, snapshot);
        this.gravity = 0.025D;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.level().playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z,
                SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1.0F, 1.2F);
        super.onHitBlock(result);
    }
}