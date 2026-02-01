package com.gamunhagol.genesismod.events;


import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class DungeonEventHandler {
    @SubscribeEvent
    public static void onDungeonActivate(GenesisDungeonEvent.Activate event) {
        ServerLevel level = event.getLevel();
        BlockPos origin = event.getPos();

        // 탐색 범위 설정
        int range = 100;



        // Stream을 사용하여 대량의 블록을 더 효율적으로 처리
        BlockPos.betweenClosedStream(origin.offset(-range, -range, -range), origin.offset(range, range, range))
                .forEach(targetPos -> {
                    // [최적화] 공기는 검사하지 말고 바로 넘어가기 (이거 한 줄이 큰 차이를 만듭니다!)
                    if (level.isEmptyBlock(targetPos)) return;

                    BlockState state = level.getBlockState(targetPos);


                    //특정 블럭을 정해진 몹으로 변환(파괴후 생성)
                    if (state.is(GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get())) {
                        level.removeBlock(targetPos, false);
                        EntityType.PIG.spawn(level, targetPos, MobSpawnType.EVENT);
                    }

                    BlockEntity be = level.getBlockEntity(targetPos);
                    if (be instanceof IFadedDungeonElement element) {
                        element.activateElement();
                    }
                });
    }
}
