package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.content.ReinforceManager;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.weapon.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisAnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.isEmpty() || !WeaponDataManager.hasData(left.getItem())) return;


        int currentLevel = 0;
        var cap = left.getCapability(WeaponStatsProvider.WEAPON_STATS).orElse(null);
        if (cap != null) {
            currentLevel = cap.getReinforceLevel();
        }

        WeaponStatData data = WeaponDataManager.get(left.getItem());
        int nextLevel = currentLevel + 1;
        int maxLevel = ReinforceManager.getMaxLevel(data.isSpecial());

        if (nextLevel > maxLevel) return;

        Item requiredMaterial = data.isSpecial()
                ? ReinforceManager.getSpecialMaterial(nextLevel)
                : ReinforceManager.getStandardMaterial(nextLevel);

        if (requiredMaterial != null && right.getItem() == requiredMaterial && right.getCount() >= 1) {
            ItemStack output = left.copy();

            final int finalNextLevel = nextLevel;
            output.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
                stats.setReinforceLevel(finalNextLevel);
            });

            event.setOutput(output);
            event.setCost(nextLevel);
            event.setMaterialCost(1);
        }
    }
}
