package com.gamunhagol.genesismod.events.world;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.entity.LightStoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class LightStoneEventHandler {

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getSpawnType() != MobSpawnType.NATURAL) return;

        if (!(event.getEntity() instanceof Monster)) return;

        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) return;

        BlockPos spawnPos = BlockPos.containing(event.getX(), event.getY(), event.getZ());

        if (level.getBrightness(LightLayer.SKY, spawnPos) <= 0) return;

        if (LightStoneBlockEntity.isSpawnBlocked(level, spawnPos)) {
            event.setSpawnCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}