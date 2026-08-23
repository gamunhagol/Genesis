package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedSkeletonEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SummonGBSkeletonSpell extends AbstractSummonSpell {

    public SummonGBSkeletonSpell() {
        super("summon_great_bow_skeleton");
    }

    @Override
    public int getCastTime() { return 40; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() { return 8.9f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedSkeletonEntity skeleton = new SummonedSkeletonEntity(GenesisEntities.SUMMONED_SKELETON.get(), level);

        ItemStack greatBow = new ItemStack(GenesisItems.GREAT_BOW.get());
        skeleton.setItemSlot(EquipmentSlot.MAINHAND, greatBow);

        skeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return skeleton;
    }
}
