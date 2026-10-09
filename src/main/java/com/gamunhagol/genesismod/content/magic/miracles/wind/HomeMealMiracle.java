package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class HomeMealMiracle extends MiracleSpell {
    private static final List<Item> STEWS = List.of(
            Items.MUSHROOM_STEW,
            Items.BEETROOT_SOUP,
            Items.RABBIT_STEW
    );

    public HomeMealMiracle() {
        super("home_meal");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override
    public float getMentalCost() {
        return 4.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!level.isClientSide && caster instanceof Player player) {
            Item randomStew = STEWS.get(level.random.nextInt(STEWS.size()));
            ItemStack stewStack = new ItemStack(randomStew);

            if (!player.getInventory().add(stewStack)) {
                player.drop(stewStack, false);
            }
        }
    }
}