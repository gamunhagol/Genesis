package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.List;

public class Collector extends WanderingTrader {
    public Collector(EntityType<? extends WanderingTrader> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setDespawnDelay(48000);
    }



    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.7D, 0.7D));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(CollectorGuard.class));

        this.goalSelector.addGoal(2, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));

        this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));

        this.goalSelector.addGoal(5, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return WanderingTrader.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.45f)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    protected void updateTrades() {
        //  기존 떠돌이 상인의 랜덤 거래 목록을 완전히 무시하고 비어있는 리스트를 가져옵니다.
        MerchantOffers merchantoffers = this.getOffers();

        int infiniteTrades = 99999;

        int copperCost = this.random.nextInt(3) + 1;
        int copperReward = this.random.nextInt(21) + 20;
        merchantoffers.add(new MerchantOffer(
                new ItemStack(Items.EMERALD, copperCost),
                new ItemStack(GenesisItems.COPPER_COIN.get(),copperReward),infiniteTrades,1, 0.7f
        ));

        int silverCost = this.random.nextInt(12) + 7;
        int silverReward = this.random.nextInt(3) + 2;
        merchantoffers.add(new MerchantOffer(
                new ItemStack(Items.EMERALD, silverCost),
                new ItemStack(GenesisItems.SILVER_COIN.get(),silverReward),infiniteTrades,3, 0.7f
        ));

        int goldCost = this.random.nextInt(10) + 6;
        int goldReward = this.random.nextInt(2) + 1;
        merchantoffers.add(new MerchantOffer(
                new ItemStack(Items.EMERALD_BLOCK, goldCost),
                new ItemStack(GenesisItems.GOLD_COIN.get(),goldReward),infiniteTrades,7, 0.7f
        ));

        int platinumCost = this.random.nextInt(50) + 9;
        merchantoffers.add(new MerchantOffer(
                new ItemStack(Items.EMERALD_BLOCK, platinumCost),
                new ItemStack(GenesisItems.PLATINUM_COIN.get(),1),infiniteTrades,15, 0.7f
        ));
    }

    private int spawnGuardCooldown = 0;

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            // 먼저 주변 가드 수를 체크
            List<CollectorGuard> guards = this.level().getEntitiesOfClass(CollectorGuard.class, this.getBoundingBox().inflate(16));

            // 가드가 4마리 미만일 때만 쿨타임을 깎습니다.
            if (guards.size() < 4) {
                if (--this.spawnGuardCooldown <= 0) {
                    this.spawnGuardCooldown = 4800;

                    CollectorGuard guard = GenesisEntities.COLLECTOR_GUARD.get().create(this.level());
                    if (guard != null) {
                        guard.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0);

                        guard.finalizeSpawn((ServerLevelAccessor)this.level(),
                                this.level().getCurrentDifficultyAt(guard.blockPosition()),
                                MobSpawnType.EVENT, null, null);

                        this.level().addFreshEntity(guard);
                    }
                }
            } else {
                this.spawnGuardCooldown = 4800;
            }
        }
    }
}
