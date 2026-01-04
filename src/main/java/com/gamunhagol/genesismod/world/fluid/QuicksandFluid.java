package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class QuicksandFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GenesisFluidTypes.QUICKSAND_TYPE, // 아까 등록한 Quicksand 전용 타입
                    GenesisFluids.QUICKSAND,          // GenesisFluids에 추가할 소스 유체
                    GenesisFluids.QUICKSAND_FLOWING   // GenesisFluids에 추가할 흐르는 유체
            )
                    .bucket(GenesisItems.QUICKSAND_BUCKET) // 양동이 아이템
                    .block(GenesisBlocks.QUICKSAND_BLOCK)  // 아까 만든 압사 로직 블록
                    .slopeFindDistance(2)  // 흐르는 거리 (유사는 멀리 못 흐르게 2로 설정)
                    .levelDecreasePerBlock(2) // 칸당 수위 감소 (빨리 끊기게 설정)
                    .tickRate(20)          // 흐르는 속도 (용암은 30, 유사는 20~30 추천. 높을수록 느림)
                    .explosionResistance(100f);
}