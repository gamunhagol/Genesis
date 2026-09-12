package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MagicGlint extends MagicBullet {
    public MagicGlint(EntityType<? extends MagicGlint> type, Level level) {
        super(type, level);
        this.homingStrength = 0.21f;
        this.homingRadius = 8.0D;
    }

    public MagicGlint(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_GLINT.get(), level, owner, snapshot);
        this.homingStrength = 0.21f;
        this.homingRadius = 8.0D;
    }
}