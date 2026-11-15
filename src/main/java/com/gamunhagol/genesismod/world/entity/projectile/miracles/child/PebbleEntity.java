package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

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

        spawnStoneParticles(8);
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        spawnStoneParticles(8);
        super.onHitEntity(result);
    }

    private void spawnStoneParticles(int count) {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState());
            serverLevel.sendParticles(particle, this.getX(), this.getY(), this.getZ(), count, 0.05D, 0.05D, 0.05D, 0.03D);
        }
    }
}
