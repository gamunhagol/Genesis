package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.world.entity.ai.DefendCollectorGoal;
import com.gamunhagol.genesismod.world.entity.ai.FollowCollectorGoal;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import yesman.epicfight.world.item.EpicFightItems;

import javax.annotation.Nullable;
import java.util.List;

public class CollectorGuard extends Monster implements RangedAttackMob, CrossbowAttackMob{
    public CollectorGuard(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super((EntityType<? extends Monster>) pEntityType, pLevel);
    }

    private int despawnTimer = 1200;

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(0, new DefendCollectorGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());



        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                (target) -> {
                    if (target instanceof CollectorGuard) return false;

                    // 주변에 징수원이 있다면, 그 징수원과 적 사이의 거리가 15블록 이내일 때만 타겟팅
                    List<Collector> collectors = this.level().getEntitiesOfClass(Collector.class, this.getBoundingBox().inflate(16.0D));
                    if (!collectors.isEmpty()) {
                        return target.distanceToSqr(collectors.get(0)) < 225.0D; // 15*15
                    }

                    return true; // 징수원이 주변에 없으면 그냥 보이면 때림
                }));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(2, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));

        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(3, new FollowCollectorGoal(this, 1.1D, 4.0F, 10.0F));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.goalSelector.addGoal(5, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }



    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);


    }
    private Item getArmorPieceByTier(int tier, EquipmentSlot slot) {
        return switch (tier) {
            case 1 -> {//가죽
                yield switch (slot) {
                    case HEAD -> Items.LEATHER_HELMET;
                    case CHEST -> Items.LEATHER_CHESTPLATE;
                    case LEGS -> Items.LEATHER_LEGGINGS;
                    default -> Items.LEATHER_BOOTS;
                };
            }
            case 2 -> {//사슬
                yield switch (slot) {
                    case HEAD -> Items.CHAINMAIL_HELMET;
                    case CHEST -> Items.CHAINMAIL_CHESTPLATE;
                    case LEGS -> Items.CHAINMAIL_LEGGINGS;
                    default -> Items.CHAINMAIL_BOOTS;
                };
            }
            case 3 -> {//가죽덧댄 사슬
                yield switch (slot) {
                    case HEAD -> GenesisItems.PADDED_CHAIN_HELMET.get();
                    case CHEST -> GenesisItems.PADDED_CHAIN_CHESTPLATE.get();
                    case LEGS -> GenesisItems.PADDED_CHAIN_LEGGINGS.get();
                    default -> GenesisItems.PADDED_CHAIN_BOOTS.get();
                };
            }
            case 4 -> {//철
                yield switch (slot) {
                    case HEAD -> Items.IRON_HELMET;
                    case CHEST -> Items.IRON_CHESTPLATE;
                    case LEGS -> Items.IRON_LEGGINGS;
                    default -> Items.IRON_BOOTS;
                };
            }
            case 5 -> {//다이아몬드
                yield switch (slot) {
                    case HEAD -> Items.DIAMOND_HELMET;
                    case CHEST -> Items.DIAMOND_CHESTPLATE;
                    case LEGS -> Items.DIAMOND_LEGGINGS;
                    default -> Items.DIAMOND_BOOTS;
                };
            }
            default -> net.minecraft.world.item.Items.AIR;
        };
    }

    private Item getWeaponPieceByTier(int tier, EquipmentSlot slot, RandomSource pRandom) {
        return switch (tier) {
            case 1 -> { // 철
                yield switch (slot) {
                    case MAINHAND -> {

                        int choice = pRandom.nextInt(4);

                        yield switch (choice) {
                            case 0 -> Items.IRON_SWORD;
                            case 1 -> EpicFightItems.IRON_SPEAR.get();
                            case 2 -> EpicFightItems.IRON_GREATSWORD.get();
                            default -> EpicFightItems.IRON_LONGSWORD.get();
                        };
                    }
                    default -> Items.AIR;
                };
            }
            case 2 -> { // 다이아몬드
                yield switch (slot) {
                    case MAINHAND -> {

                        int choice = pRandom.nextInt(4);

                        yield switch (choice) {
                            case 0 -> Items.DIAMOND_SWORD;
                            case 1 -> EpicFightItems.DIAMOND_SPEAR.get();
                            case 2 -> EpicFightItems.DIAMOND_GREATSWORD.get();
                            default -> EpicFightItems.DIAMOND_LONGSWORD.get();
                        };
                    }
                    default -> Items.AIR;
                };
            }
            case 3 -> { // 원거리
                yield switch (slot) {
                    case MAINHAND -> {

                        int choice = pRandom.nextInt(2);

                        yield switch (choice) {
                            case 0 -> Items.BOW;
                            default -> Items.CROSSBOW;
                        };
                    }
                    default -> Items.AIR;
                };
            }
            default -> Items.AIR;
        };
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        // 1. 갑옷 부위별로 따로 티어를 굴립니다 (0~5)
        // 0이 나오면 위 메서드에서 AIR를 반환하므로 아무것도 입지 않습니다.
        int armorTier = pRandom.nextInt(6);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.HEAD)));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.CHEST)));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.LEGS)));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.FEET)));

        int weaponTier = pRandom.nextInt(3) + 1;
        ItemStack mainHand = new ItemStack(getWeaponPieceByTier(weaponTier, EquipmentSlot.MAINHAND, pRandom));

        this.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);

        // 드랍 확률
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!this.getItemBySlot(slot).isEmpty()) {
                this.setDropChance(slot, 0.45F);
            }
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData data = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

        // 소환될 때 장비를 입히는 메서드를 직접 호출해줘야 합니다.
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);

        return data;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        // 화살 객체 생성
        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, new ItemStack(Items.ARROW), pVelocity);

        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        // 타겟을 향해 발사
        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(CollectorGuard.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    // 장전 상태 설정 및 가져오기
    public void setChargingCrossbow(boolean pIsCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, pIsCharging);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void onCrossbowShot() {
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, Projectile pProjectile, float pProjectileAngle) {
        // 실제로 투사체를 쏘는 로직
        this.shootCrossbowProjectile(this, pTarget, pProjectile, pProjectileAngle, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void die(DamageSource pCause) {
        if (pCause.getEntity() instanceof Zombie && !this.level().isClientSide && this.random.nextFloat() < 0.5f) {
            // 좀비로 변신시키는 로직
            Zombie zombie = EntityType.ZOMBIE.create(this.level());
            if (zombie != null) {
                zombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                zombie.setNoAi(this.isNoAi());
                this.level().addFreshEntity(zombie);
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack guardItem = this.getItemBySlot(slot); // 호위병이 입고 있는 아이템 가져오기
                if (!guardItem.isEmpty()) {
                    // 아이템을 복사해서 좀비에게 입힙니다. (.copy()를 꼭 써야 에러가 안 납니다)
                    zombie.setItemSlot(slot, guardItem.copy());


                    zombie.setDropChance(slot, 0.45F);
                }
                if (this.hasCustomName()) {
                    zombie.setCustomName(this.getCustomName());
                    zombie.setCustomNameVisible(this.isCustomNameVisible());
                }
            }
        }
        super.die(pCause);
    }
    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (super.isAlliedTo(pEntity)) {
            return true;
        } else
            if (pEntity == this || pEntity instanceof CollectorGuard) return true;
            if (pEntity instanceof IronGolem || pEntity instanceof Villager) {
            return true;
        } else if (pEntity instanceof Collector) {
            return true;

        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            // 성능을 위해 1초(20틱)마다 징수원 존재 여부 체크
            if (this.tickCount % 20 == 0) {
                List<Collector> list = this.level().getEntitiesOfClass(Collector.class, this.getBoundingBox().inflate(16.0D));

                if (list.isEmpty()) {
                    // 징수원이 없으면 타이머에서 20틱(1초) 차감
                    this.despawnTimer -= 20;
                } else {
                    // 징수원이 다시 나타나면 타이머 리셋
                    this.despawnTimer = 1200;
                }
            }

            // 타이머가 0 이하가 되면 소멸
            if (this.despawnTimer <= 0) {
                this.discard();
            }
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("DespawnTimer", this.despawnTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("DespawnTimer")) {
            this.despawnTimer = pCompound.getInt("DespawnTimer");
        }
    }
}
