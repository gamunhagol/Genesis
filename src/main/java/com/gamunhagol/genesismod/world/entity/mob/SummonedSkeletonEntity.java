package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.world.entity.ai.SummonedAIGoals;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import com.gamunhagol.genesismod.world.entity.base.SummonHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SummonedSkeletonEntity extends Skeleton implements ISummonable {

    private UUID ownerUUID;
    private final float upkeepCost = 0.0f;

    public SummonedSkeletonEntity(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new SummonedAIGoals.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));

        this.targetSelector.addGoal(1, new SummonedAIGoals.OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new SummonedAIGoals.OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                (target) -> !(target instanceof ISummonable)));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = SummonHelper.handleInteraction(this, player, hand);
        if (result.consumesAction()) {
            return result;
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() { return this.ownerUUID; }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) { this.ownerUUID = uuid; }

    @Override
    public float getUpkeepCost() { return this.upkeepCost; }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.ownerUUID != null) {
            compound.putUUID("SummonOwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("SummonOwnerUUID")) {
            this.ownerUUID = compound.getUUID("SummonOwnerUUID");
        }
    }
}