package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedStrayEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class SummonStraySpell extends AbstractSummonSpell {

    public SummonStraySpell() {
        super("summon_stray");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() {
        return 8.8f;
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
        SummonedStrayEntity stray = new SummonedStrayEntity(GenesisEntities.SUMMONED_STRAY.get(), level);

        stray.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        stray.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return stray;
    }
}