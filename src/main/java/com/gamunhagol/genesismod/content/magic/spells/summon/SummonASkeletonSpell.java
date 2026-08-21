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
import net.minecraft.world.item.Items;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.Map;

public class SummonASkeletonSpell extends AbstractSummonSpell {

    public SummonASkeletonSpell() {
        super("summon_armored_skeleton");
    }

    @Override
    public int getCastTime() { return 40; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.ARCANE, 15,
                StatType.INTELLIGENCE, 19);
    }

    @Override
    public float getMentalCost() { return 13.5f; }

    @Override
    public int getMemoryCost() { return 3; }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedSkeletonEntity skeleton = new SummonedSkeletonEntity(GenesisEntities.SUMMONED_SKELETON.get(), level);

        skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        skeleton.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        skeleton.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        skeleton.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));

        int weaponChoice = level.random.nextInt(5);
        ItemStack weaponToEquip;
        switch (weaponChoice) {
            case 0: weaponToEquip = new ItemStack(Items.IRON_SWORD); break;
            case 1: weaponToEquip = new ItemStack(EpicFightItems.IRON_GREATSWORD.get()); break;
            case 2: weaponToEquip = new ItemStack(EpicFightItems.IRON_SPEAR.get()); break;
            case 3: weaponToEquip = new ItemStack(Items.BOW); break;
            case 4: default: weaponToEquip = new ItemStack(GenesisItems.GREAT_BOW.get()); break;
        }
        skeleton.setItemSlot(EquipmentSlot.MAINHAND, weaponToEquip);

        skeleton.setDropChance(EquipmentSlot.HEAD, 0.0f);
        skeleton.setDropChance(EquipmentSlot.CHEST, 0.0f);
        skeleton.setDropChance(EquipmentSlot.LEGS, 0.0f);
        skeleton.setDropChance(EquipmentSlot.FEET, 0.0f);
        skeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return skeleton;
    }
}