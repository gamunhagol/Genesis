package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedWitherSkeletonEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class SummonWitherSkeletonSpell extends AbstractSummonSpell {

    public SummonWitherSkeletonSpell() {
        super("summon_wither_skeleton");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 16);
    }

    @Override
    public float getMentalCost() {
        return 11.5f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedWitherSkeletonEntity witherSkeleton = new SummonedWitherSkeletonEntity(GenesisEntities.SUMMONED_WITHER_SKELETON.get(), level);

        witherSkeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        witherSkeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return witherSkeleton;
    }
}