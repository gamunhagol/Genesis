package com.gamunhagol.genesismod.events.world;


import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
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

        int range = 60;



        BlockPos.betweenClosedStream(origin.offset(-range, -range, -range), origin.offset(range, range, range))
                .forEach(targetPos -> {
                    if (level.isEmptyBlock(targetPos)) return;

                    BlockState state = level.getBlockState(targetPos);


                    if (state.is(GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get())) {
                        level.removeBlock(targetPos, false);
                        EntityType.PIG.spawn(level, targetPos, MobSpawnType.EVENT);
                    }

                    if (state.is(GenesisBlocks.STATUE_OF_HERALD_OF_OBLIVION.get())) {
                        level.removeBlock(targetPos, false);
                        EntityType.COW.spawn(level, targetPos, MobSpawnType.EVENT);
                    }

                    if (state.is(GenesisBlocks.STATUE_OF_GUIDE_TO_OBLIVION.get())) {
                        level.removeBlock(targetPos, false);
                        EntityType.SHEEP.spawn(level, targetPos, MobSpawnType.EVENT);
                    }

                    BlockEntity be = level.getBlockEntity(targetPos);
                    if (be instanceof IFadedDungeonElement element) {
                        element.activateElement();
                    }
                });
    }
}
