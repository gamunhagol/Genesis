package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.world.entity.ai.DefendCollectorGoal;
import com.gamunhagol.genesismod.world.entity.ai.FollowCollectorGoal;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.List;

public class CollectorGuard extends AbstractWanderer {

    private int despawnTimer = 1200;

    public CollectorGuard(EntityType<? extends AbstractWanderer> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        // 1. 부모 클래스의 공통 AI(기본 이동, 몬스터 사냥 등)를 먼저 등록합니다.
        super.registerGoals();

        // 2. 호위병 전용 특수 AI 추가 (우선순위 조절)
        this.targetSelector.addGoal(0, new DefendCollectorGoal(this));

        // 징수원 주변에서만 몬스터를 타겟팅하는 특수 조건 (기존 로직 유지)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                (target) -> {
                    if (target instanceof AbstractWanderer) return false; // 모든 방랑자 세력 보호

                    List<Collector> collectors = this.level().getEntitiesOfClass(Collector.class, this.getBoundingBox().inflate(16.0D));
                    if (!collectors.isEmpty()) {
                        return target.distanceToSqr(collectors.get(0)) < 225.0D; // 15블록 이내
                    }
                    return true;
                }));

        this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(3, new FollowCollectorGoal(this, 1.1D, 4.0F, 10.0F));
        this.goalSelector.addGoal(5, new InteractGoal(this, Player.class, 3.0F, 1.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ARMOR, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    // --- 아군 판정 확장 ---
    @Override
    public boolean isAlliedTo(Entity pEntity) {
        // 부모 클래스(AbstractWanderer)에서 이미 플레이어, 골렘, 주민을 아군으로 설정함
        return super.isAlliedTo(pEntity) || pEntity instanceof Collector;
    }

    // --- 호위병 전용: 징수원 부재 시 소멸 로직 ---
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            List<Collector> list = this.level().getEntitiesOfClass(Collector.class, this.getBoundingBox().inflate(24.0D));
            if (list.isEmpty()) {
                this.despawnTimer -= 20;
            } else {
                this.despawnTimer = 1200;
            }

            if (this.despawnTimer <= 0) {
                this.discard();
            }
        }
    }

    // --- 호위병 전용: 사망 시 좀비 변이 로직 ---
    @Override
    public void die(DamageSource pCause) {
        if (pCause.getEntity() instanceof Zombie && !this.level().isClientSide && this.random.nextFloat() < 0.5f) {
            Zombie zombie = EntityType.ZOMBIE.create(this.level());
            if (zombie != null) {
                zombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                zombie.setNoAi(this.isNoAi());

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack guardItem = this.getItemBySlot(slot);
                    if (!guardItem.isEmpty()) {
                        zombie.setItemSlot(slot, guardItem.copy());
                        zombie.setDropChance(slot, 0.05F);
                    }
                }
                if (this.hasCustomName()) {
                    zombie.setCustomName(this.getCustomName());
                    zombie.setCustomNameVisible(this.isCustomNameVisible());
                }
                this.level().addFreshEntity(zombie);
            }
        }
        super.die(pCause);
    }

    // --- 장비 티어 결정 로직 (호위병 고유 특징) ---
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        int armorTier = pRandom.nextInt(6);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.HEAD)));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.CHEST)));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.LEGS)));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(getArmorPieceByTier(armorTier, EquipmentSlot.FEET)));

        int weaponTier = pRandom.nextInt(3) + 1;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(getWeaponPieceByTier(weaponTier, EquipmentSlot.MAINHAND, pRandom)));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!this.getItemBySlot(slot).isEmpty()) {
                this.setDropChance(slot, 0.45F);
            }
        }
    }

    private Item getArmorPieceByTier(int tier, EquipmentSlot slot) {
        return switch (tier) {
            case 1 -> switch (slot) {
                case HEAD -> Items.LEATHER_HELMET; case CHEST -> Items.LEATHER_CHESTPLATE;
                case LEGS -> Items.LEATHER_LEGGINGS; default -> Items.LEATHER_BOOTS;
            };
            case 2 -> switch (slot) {
                case HEAD -> Items.CHAINMAIL_HELMET; case CHEST -> Items.CHAINMAIL_CHESTPLATE;
                case LEGS -> Items.CHAINMAIL_LEGGINGS; default -> Items.CHAINMAIL_BOOTS;
            };
            case 3 -> switch (slot) {
                case HEAD -> GenesisItems.PADDED_CHAIN_HELMET.get(); case CHEST -> GenesisItems.PADDED_CHAIN_CHESTPLATE.get();
                case LEGS -> GenesisItems.PADDED_CHAIN_LEGGINGS.get(); default -> GenesisItems.PADDED_CHAIN_BOOTS.get();
            };
            case 4 -> switch (slot) {
                case HEAD -> Items.IRON_HELMET; case CHEST -> Items.IRON_CHESTPLATE;
                case LEGS -> Items.IRON_LEGGINGS; default -> Items.IRON_BOOTS;
            };
            case 5 -> switch (slot) {
                case HEAD -> Items.DIAMOND_HELMET; case CHEST -> Items.DIAMOND_CHESTPLATE;
                case LEGS -> Items.DIAMOND_LEGGINGS; default -> Items.DIAMOND_BOOTS;
            };
            default -> Items.AIR;
        };
    }

    private Item getWeaponPieceByTier(int tier, EquipmentSlot slot, RandomSource pRandom) {
        if (slot != EquipmentSlot.MAINHAND) return Items.AIR;
        return switch (tier) {
            case 1 -> switch (pRandom.nextInt(4)) {
                case 0 -> Items.IRON_SWORD; case 1 -> EpicFightItems.IRON_SPEAR.get();
                case 2 -> EpicFightItems.IRON_GREATSWORD.get(); default -> EpicFightItems.IRON_LONGSWORD.get();
            };
            case 2 -> switch (pRandom.nextInt(4)) {
                case 0 -> Items.DIAMOND_SWORD; case 1 -> EpicFightItems.DIAMOND_SPEAR.get();
                case 2 -> EpicFightItems.DIAMOND_GREATSWORD.get(); default -> EpicFightItems.DIAMOND_LONGSWORD.get();
            };
            case 3 -> pRandom.nextBoolean() ? Items.BOW : Items.CROSSBOW;
            default -> Items.AIR;
        };
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("DespawnTimer", this.despawnTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("DespawnTimer")) this.despawnTimer = pCompound.getInt("DespawnTimer");
    }

    protected int getExperienceReward(Player player) { return 5 + this.random.nextInt(5); }
}