package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.world.entity.ai.WandererCrossbowAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class AbstractWanderer extends PathfinderMob implements RangedAttackMob, CrossbowAttackMob {

    protected static final int INVENTORY_SIZE = 8;
    protected final SimpleContainer inventory = new SimpleContainer(INVENTORY_SIZE);

    //  누락되었던 데이터 시리얼라이저 정의
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(AbstractWanderer.class, EntityDataSerializers.BOOLEAN);

    protected AbstractWanderer(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // 커스텀 석궁 Goal 및 바닐라 활 Goal 사용
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(2, new WandererCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // 타겟팅: 모든 몬스터를 공격하되, 방랑자 세력은 서로 공격하지 않음
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                (target) -> !(target instanceof AbstractWanderer)));
    }

    // . 데이터 동기화 설정
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public void setChargingCrossbow(boolean pIsCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, pIsCharging);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }


    // 무기에 맞는 발사체 정보 가져오기 (Monster 클래스의 로직 복제)
    public ItemStack getProjectile(ItemStack pShootable) {
        if (pShootable.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return ForgeHooks.getProjectile(this, pShootable, itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack);
        } else {
            return ForgeHooks.getProjectile(this, pShootable, ItemStack.EMPTY);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        // 현재 손에 든 무기에 맞는 화살 생성
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        ItemStack arrowStack = this.getProjectile(weapon);

        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, arrowStack, pVelocity);

        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, Projectile pProjectile, float pProjectileAngle) {
        this.shootCrossbowProjectile(this, pTarget, pProjectile, pProjectileAngle, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() { }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        // 만약 지금 나를 때린 놈이 pEntity라면, 더 이상 아군이 아니다! (복수 모드)
        if (pEntity == this.getLastHurtByMob()) {
            return false;
        }

        // 그 외의 경우에는 기존 아군 로직 유지
        if (super.isAlliedTo(pEntity)) return true;

        return pEntity instanceof AbstractWanderer ||
                pEntity instanceof Player ||
                pEntity instanceof Villager ||
                pEntity instanceof IronGolem;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // 인벤토리를 NBT 리스트로 변환하여 저장
        pCompound.put("Inventory", this.inventory.createTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        // NBT에서 데이터를 읽어와 인벤토리에 채움
        this.inventory.fromTag(pCompound.getList("Inventory", 10));
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        // 인벤토리의 모든 아이템을 월드에 뿌림
        Containers.dropContents(this.level(), this, this.inventory);
    }

    @Override
    public boolean canPickUpLoot() {
        return true; // 아이템을 주울 수 있게 설정
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack itemstack = pItemEntity.getItem();
        // 인벤토리에 아이템 추가 시도
        ItemStack remainingStack = this.inventory.addItem(itemstack);

        if (remainingStack.isEmpty()) {
            pItemEntity.discard(); // 모두 주웠으면 아이템 엔티티 제거
        } else {
            itemstack.setCount(remainingStack.getCount()); // 남은 만큼만 다시 설정
        }
    }

    // --- 기본 설정 및 스폰 ---
    @Override public SoundSource getSoundSource() { return SoundSource.NEUTRAL; }
    @Override protected SoundEvent getSwimSound() { return SoundEvents.PLAYER_SWIM; }
    @Override protected SoundEvent getHurtSound(DamageSource pDamageSource) { return SoundEvents.PLAYER_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.PLAYER_DEATH; }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}