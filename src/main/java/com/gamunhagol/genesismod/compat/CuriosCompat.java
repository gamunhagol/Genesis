package com.gamunhagol.genesismod.compat;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Curios API와의 연동을 전담하는 클래스입니다.
 * 이 클래스의 메서드는 반드시 ModList.get().isLoaded("curios") 체크 후에 호출되어야 합니다.
 */
public class CuriosCompat {
    /**
     * 특정 엔티티가 특정 아이템을 Curios 슬롯에 장착하고 있는지 확인합니다.
     */
    public static boolean isTrinketEquipped(LivingEntity entity, net.minecraft.world.item.Item trinket) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, trinket).isPresent();
    }

    /**
     * 나중에 아이템의 특수 효과(Tick) 등을 처리할 때 사용할 수 있는 공간입니다.
     */
    public static void handleCurioTick(LivingEntity entity, ItemStack stack) {
        // 장신구 장착 시 매 틱마다 실행될 로직을 여기에 작성합니다.
    }
}
