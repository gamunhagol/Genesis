package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedSkeletonEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SummonSkeletonSlaveSpell extends AbstractSummonSpell {

    public SummonSkeletonSlaveSpell() {
        super("summon_skeleton_slave");
    }

    @Override
    public int getCastTime() { return 40; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.ARCANE, 9,
                StatType.INTELLIGENCE, 14);
    }

    @Override
    public float getMentalCost() { return 3.5f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedSkeletonEntity skeleton = new SummonedSkeletonEntity(GenesisEntities.SUMMONED_SKELETON.get(), level);

        skeleton.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        return skeleton;
    }
}