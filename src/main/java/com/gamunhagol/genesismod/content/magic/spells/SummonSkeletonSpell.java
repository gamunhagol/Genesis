package com.gamunhagol.genesismod.content.magic.spells;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedSkeletonEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class SummonSkeletonSpell extends AbstractSummonSpell {

    public SummonSkeletonSpell() {
        super("summon_skeleton");
    }

    @Override
    public int getCastTime() { return 40; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.ARCANE, 12,
                StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() { return 5.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedSkeletonEntity skeleton = new SummonedSkeletonEntity(GenesisEntities.SUMMONED_SKELETON.get(), level);

        skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        skeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return skeleton;
    }
}
