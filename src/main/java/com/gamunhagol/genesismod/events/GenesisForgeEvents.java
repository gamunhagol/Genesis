package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import com.gamunhagol.genesismod.world.spawner.CollectorSpawner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "genesis", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisForgeEvents {
    private static final CollectorSpawner COLLECTOR_SPAWNER = new CollectorSpawner();

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        // 서버 월드 틱의 마지막 시점에만 실행
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == Level.OVERWORLD) {
                COLLECTOR_SPAWNER.tick(serverLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        // 1. 일반적인 몬스터(스켈레톤, 거미, 좀비 등)와 약탈자(Pillager, Vindicator 등)인지 확인
        if (entity instanceof Monster monster) {
            // 단, 호위병 자기 자신은 제외해야 서로 싸우지 않습니다.
            if (!(monster instanceof CollectorGuard)) {
                monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, CollectorGuard.class, true));
            }
        }
    }
}
