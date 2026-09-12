package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicTrap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagicMist extends MagicTrap implements ItemSupplier {
    public MagicMist(EntityType<? extends MagicMist> type, Level level) {
        super(type, level);
        this.triggerRadius = 0.8D;
        this.maxLifeTicks = 200;
    }

    public MagicMist(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_MIST.get(), level, owner, snapshot);
        this.triggerRadius = 0.8D;
        this.maxLifeTicks = 200;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }
}