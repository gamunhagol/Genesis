package com.gamunhagol.genesismod.events.client;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisArmorItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorTooltipHandler {

    @SubscribeEvent
    public static void onArmorTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof GenesisArmorItem)) return;

        List<Component> tooltip = event.getToolTip();

        Component fireLine = removeAndGetLine(tooltip, "fire_defense");
        Component frostLine = removeAndGetLine(tooltip, "frost_defense");
        Component lightningLine = removeAndGetLine(tooltip, "lightning_defense");
        Component magicLine = removeAndGetLine(tooltip, "magic_defense");
        Component holyLine = removeAndGetLine(tooltip, "holy_defense");

        int insertIndex = findArmorIndex(tooltip);
        int currentIndex = insertIndex;

        if (fireLine != null) {
            tooltip.add(currentIndex, fireLine);
            currentIndex++;
        }
        if (frostLine != null) {
            tooltip.add(currentIndex, frostLine);
            currentIndex++;
        }
        if (lightningLine != null) {
            tooltip.add(currentIndex, lightningLine);
            currentIndex++;
        }
        if (magicLine != null) {
            tooltip.add(currentIndex, magicLine);
            currentIndex++;
        }
        if (holyLine != null) {
            tooltip.add(currentIndex, holyLine);
            currentIndex++;
        }
    }

    private static int findArmorIndex(List<Component> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            String content = tooltip.get(i).getString();
            if (content.contains("방어") || content.contains("Armor")) return i + 1;
        }
        return tooltip.size();
    }

    private static Component removeAndGetLine(List<Component> tooltip, String attributeKey) {
        Iterator<Component> iterator = tooltip.iterator();
        while (iterator.hasNext()) {
            Component component = iterator.next();
            if (component.getString().contains(attributeKey) ||
                    component.toString().contains(attributeKey)) {
                iterator.remove();
                return component;
            }
        }
        return null;
    }
}