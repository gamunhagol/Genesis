package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.world.entity.base.AbstractWanderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Predicate;

public class ZombieInfectionHandler {

    public static final String TAG_INFECTED = "GenesisInfected";
    public static final String TAG_TIMER = "GenesisInfectionTimer";
    public static final int DEFAULT_MAX_TICKS = 6000;



    public static boolean canBeInfected(LivingEntity entity) {
        if (!entity.isAlive() || isImmune(entity)) return false;
        return entity instanceof Villager
                || entity instanceof AbstractPiglin
                || entity instanceof Hoglin
                || entity instanceof AbstractWanderer;
    }

    public static void infect(LivingEntity entity) {
        if (!canBeInfected(entity)) return;
        CompoundTag data = entity.getPersistentData();
        if (!data.getBoolean(TAG_INFECTED)) {
            data.putBoolean(TAG_INFECTED, true);
            data.putInt(TAG_TIMER, 0);
        }
    }


    public static void cure(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        if (data.getBoolean(TAG_INFECTED)) {
            data.remove(TAG_INFECTED);
            data.remove(TAG_TIMER);
        }
    }



    public static boolean isCureItem(ItemStack stack) {
        return stack.is(Items.ENCHANTED_GOLDEN_APPLE);
    }


    public static boolean shouldCureByCondition(LivingEntity entity) {

        return false;
    }




    public static boolean isImmune(LivingEntity entity) {
        return hasMatchingItem(entity, ZombieInfectionHandler::isImmunityItem);
    }


    public static boolean isInfectionPaused(LivingEntity entity) {

        return hasMatchingItem(entity, ZombieInfectionHandler::isPauseItem);
    }

    public static int getRequiredTicks(LivingEntity entity) {
        int ticks = DEFAULT_MAX_TICKS;

        ticks += getTotalItemDelayTicks(entity);

        return ticks;
    }


    public static boolean isImmunityItem(ItemStack stack) {
        // 추후 완전 면역 갑옷/악세서리/아이템 추가 시 조건 작성
        return false;
    }

    public static boolean isPauseItem(ItemStack stack) {
        // 추후 보유 시 감염 틱을 멈추는 갑옷/악세서리/아이템 추가 시 조건 작성
        return false;
    }

    public static int getItemDelayTicks(ItemStack stack) {
        // 추후 변이 시간을 늦추는 갑옷/악세서리/아이템별 추가 틱 반환
        // 예: if (stack.is(...)) return 1200; // 1분 지연
        return 0;
    }



    private static boolean hasMatchingItem(LivingEntity entity, Predicate<ItemStack> predicate) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return true;
            }
        }

        if (entity instanceof net.minecraft.world.entity.npc.InventoryCarrier carrier) {
            var inv = carrier.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    return true;
                }
            }
        }
        // 참고: AbstractWanderer에 public SimpleContainer getInventory() 메서드를 추가하시거나
        // InventoryCarrier 인터페이스를 implements 하시면 방랑자 인벤토리도 자동 검사됩니다.

        //  Curios 악세서리 슬롯 연동 지점 (compat 패키지의 CuriosCompat 호출용)
        // 예: if (CuriosCompat.hasMatchingCurio(entity, predicate)) return true;

        return false;
    }

    private static int getTotalItemDelayTicks(LivingEntity entity) {
        int bonus = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                bonus += getItemDelayTicks(stack);
            }
        }

        if (entity instanceof net.minecraft.world.entity.npc.InventoryCarrier carrier) {
            var inv = carrier.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty()) {
                    bonus += getItemDelayTicks(stack);
                }
            }
        }

        // Curios 악세서리 슬롯 연동 지점
        // 예: bonus += CuriosCompat.getCurioDelayTicks(entity, ZombieInfectionHandler::getItemDelayTicks);

        return bonus;
    }


    public static void tickInfection(LivingEntity entity) {
        if (entity.level().isClientSide() || !entity.isAlive()) return;

        CompoundTag data = entity.getPersistentData();
        if (!data.getBoolean(TAG_INFECTED)) return;

        if (shouldCureByCondition(entity) || isImmune(entity)) {
            cure(entity);
            return;
        }

        if (isInfectionPaused(entity)) {
            return;
        }

        int timer = data.getInt(TAG_TIMER) + 1;
        int requiredTicks = getRequiredTicks(entity);

        if (timer >= requiredTicks) {
            data.remove(TAG_INFECTED);
            data.remove(TAG_TIMER);
            convert((ServerLevel) entity.level(), entity);
        } else {
            data.putInt(TAG_TIMER, timer);
        }
    }

    private static void convert(ServerLevel level, LivingEntity target) {
        if (target instanceof Villager villager) {
            ZombieVillager zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
            if (zombieVillager != null) {
                zombieVillager.finalizeSpawn(level, level.getCurrentDifficultyAt(zombieVillager.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), null);
                zombieVillager.setVillagerData(villager.getVillagerData());
                zombieVillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                zombieVillager.setTradeOffers(villager.getOffers().createTag());
                zombieVillager.setVillagerXp(villager.getVillagerXp());
            }
        }
        else if (target instanceof AbstractPiglin piglin) {
            piglin.convertTo(EntityType.ZOMBIFIED_PIGLIN, true);
        }
        else if (target instanceof Hoglin hoglin) {
            hoglin.convertTo(EntityType.ZOGLIN, true);
        }
        else if (target instanceof AbstractWanderer wanderer) {
            Zombie zombie = EntityType.ZOMBIE.create(level);
            if (zombie != null) {
                zombie.moveTo(wanderer.getX(), wanderer.getY(), wanderer.getZ(), wanderer.getYRot(), wanderer.getXRot());
                zombie.setNoAi(wanderer.isNoAi());

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack stack = wanderer.getItemBySlot(slot);
                    if (!stack.isEmpty()) {
                        zombie.setItemSlot(slot, stack.copy());
                        zombie.setDropChance(slot, 0.05F);
                    }
                }

                if (wanderer.hasCustomName()) {
                    zombie.setCustomName(wanderer.getCustomName());
                    zombie.setCustomNameVisible(wanderer.isCustomNameVisible());
                }

                level.addFreshEntity(zombie);
                wanderer.discard();
            }
        }
    }
}
