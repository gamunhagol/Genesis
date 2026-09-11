package com.gamunhagol.genesismod.world.item.accessories;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import yesman.epicfight.skill.modules.ChargeableSkill;
import yesman.epicfight.skill.modules.HoldableSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;


public class ChargeStoneItem extends Item {

    public ChargeStoneItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof Player player)) {
            return;
        }

        if (player.getInventory().findSlotMatchingItem(stack) != slotId) {
            return;
        }

        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (playerPatch == null) {
            return;
        }

        HoldableSkill holdingSkill = playerPatch.getHoldingSkill();
        if (holdingSkill instanceof ChargeableSkill chargeableSkill) {
            int currentCharge = playerPatch.getChargingAmount();
            int maxCharge = chargeableSkill.getAllowedMaxChargingTicks();

            if (currentCharge < maxCharge) {
                // 매 틱 +1틱 추가 누적 (기본 1틱 + 추가 1틱 = 2배속 / 약 50% 시간 단축)
                // 만약 정확히 1.5배(50% 가속)를 원하면 (player.tickCount % 2 == 0) 조건 추가 가능
                playerPatch.setChargingAmount(Math.min(currentCharge + 1, maxCharge));
            }
        }
    }
}