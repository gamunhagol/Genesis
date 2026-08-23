package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedZombieEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.Map;

public class SummonAZombieSpell extends AbstractSummonSpell {

    public SummonAZombieSpell() {
        super("summon_armored_zombie");
    }

    @Override
    public int getCastTime() {
        return 40;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 17);
    }

    @Override
    public float getMentalCost() {
        return 13.5f;
    }

    @Override
    public int getMemoryCost() {
        return 3;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedZombieEntity zombie = new SummonedZombieEntity(GenesisEntities.SUMMONED_ZOMBIE.get(), level);

        zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        zombie.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        zombie.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        zombie.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));

        int weaponChoice = level.random.nextInt(3);
        ItemStack weaponToEquip;
        switch (weaponChoice) {
            case 0:
                weaponToEquip = new ItemStack(Items.IRON_SWORD);
                break;
            case 1:
                weaponToEquip = new ItemStack(EpicFightItems.IRON_GREATSWORD.get());
                break;
            case 2:
            default:
                weaponToEquip = new ItemStack(EpicFightItems.IRON_LONGSWORD.get());
                break;
        }
        zombie.setItemSlot(EquipmentSlot.MAINHAND, weaponToEquip);

        zombie.setDropChance(EquipmentSlot.HEAD, 0.0f);
        zombie.setDropChance(EquipmentSlot.CHEST, 0.0f);
        zombie.setDropChance(EquipmentSlot.LEGS, 0.0f);
        zombie.setDropChance(EquipmentSlot.FEET, 0.0f);
        zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return zombie;
    }
}