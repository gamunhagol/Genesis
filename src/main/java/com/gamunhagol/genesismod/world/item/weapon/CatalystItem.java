package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CatalystItem extends Item {
    public CatalystItem(Properties properties) {
        super(properties);
    }

    protected AbstractSpell getSelectedSpell(Player player) {
        // 나중에 슬롯 시스템에서 가져올 부분
        return GenesisSpells.HEAL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        AbstractSpell currentSpell = getSelectedSpell(player);
        ItemStack catalyst = player.getItemInHand(hand);

        if (currentSpell.canCast(player)) {
            if (!level.isClientSide) {
                // WeaponRequirementHelper를 통해 촉매의 보정치가 포함된 스냅샷 계산
                DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(player, catalyst, 0f);

                //  메서드 이름을 executeCast로, 파라미터 개수를 3개로 맞춤
                currentSpell.executeCast(level, player, catalystPower);
            }
            return InteractionResultHolder.sidedSuccess(catalyst, level.isClientSide());
        } else {
            //임시용 실패 사운드
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResultHolder.fail(catalyst);
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pToRepair.is(GenesisItems.AMETHYST_WAND.get())) {
            return pRepair.is(Items.AMETHYST_SHARD);
        }
        if (pToRepair.is(GenesisItems.GREEN_STAR_SEAL.get())) {
            return pRepair.is(Items.EMERALD);
        }

        return super.isValidRepairItem(pToRepair, pRepair);
    }
}