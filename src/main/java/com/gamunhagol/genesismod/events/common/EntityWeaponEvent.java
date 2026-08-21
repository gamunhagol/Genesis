package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.entity.ai.GreatBowAttackGoal;
import com.gamunhagol.genesismod.world.item.weapon.GreatBowItem;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityWeaponEvent {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof AbstractSkeleton skeleton) {
            updateSkeletonAI(skeleton);
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof AbstractSkeleton skeleton) {
            updateSkeletonAI(skeleton);
        }
    }

    private static void updateSkeletonAI(AbstractSkeleton skeleton) {
        if (skeleton.getMainHandItem().getItem() instanceof GreatBowItem) {
            List<Goal> toRemove = new ArrayList<>();
            skeleton.goalSelector.getAvailableGoals().forEach(wrapped -> {
                Goal g = wrapped.getGoal();
                if (g instanceof MeleeAttackGoal || g instanceof RangedBowAttackGoal) {
                    toRemove.add(g);
                }
            });
            toRemove.forEach(skeleton.goalSelector::removeGoal);

            boolean hasGreatBowGoal = skeleton.goalSelector.getAvailableGoals().stream()
                    .anyMatch(wrapped -> wrapped.getGoal() instanceof GreatBowAttackGoal);

            if (!hasGreatBowGoal) {
                skeleton.goalSelector.addGoal(4, new GreatBowAttackGoal<>(skeleton, 1.0D, 25.0F));
            }
        }
        else {
            boolean hasGreatBowGoal = skeleton.goalSelector.getAvailableGoals().stream()
                    .anyMatch(wrapped -> wrapped.getGoal() instanceof GreatBowAttackGoal);

            if (hasGreatBowGoal) {
                List<Goal> toRemove = new ArrayList<>();
                skeleton.goalSelector.getAvailableGoals().forEach(wrapped -> {
                    if (wrapped.getGoal() instanceof GreatBowAttackGoal) {
                        toRemove.add(wrapped.getGoal());
                    }
                });
                toRemove.forEach(skeleton.goalSelector::removeGoal);

                skeleton.reassessWeaponGoal();
            }
        }
    }
}