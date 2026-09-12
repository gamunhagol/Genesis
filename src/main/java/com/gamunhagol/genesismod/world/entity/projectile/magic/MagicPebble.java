package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MagicPebble extends MagicBullet {
    public MagicPebble(EntityType<? extends MagicPebble> type, Level level) {
        super(type, level);
        this.homingStrength = 0.17f;
        this.homingRadius = 12.0D;
    }

    public MagicPebble(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_PEBBLE.get(), level, owner, snapshot);
        this.homingStrength = 0.17f;
        this.homingRadius = 12.0D;
    }
}
