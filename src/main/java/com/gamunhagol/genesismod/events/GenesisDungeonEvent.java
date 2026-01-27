package com.gamunhagol.genesismod.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;

public class GenesisDungeonEvent extends Event {
    private final ServerLevel level;
    private final BlockPos pos;

    public GenesisDungeonEvent(ServerLevel level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public ServerLevel getLevel() { return level; }
    public BlockPos getPos() { return pos; }

    // 던전 활성화용 내부 클래스
    public static class Activate extends GenesisDungeonEvent {
        public Activate(ServerLevel level, BlockPos pos) {
            super(level, pos);
        }
    }
}