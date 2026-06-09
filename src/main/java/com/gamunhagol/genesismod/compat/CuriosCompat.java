package com.gamunhagol.genesismod.compat;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;


public class CuriosCompat {

    public static boolean isTrinketEquipped(LivingEntity entity, net.minecraft.world.item.Item trinket) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, trinket).isPresent();
    }


    public static void handleCurioTick(LivingEntity entity, ItemStack stack) {
    }
}
