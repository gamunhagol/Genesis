package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CatalystItem extends Item {
    public CatalystItem(Properties properties) {
        super(properties);
    }

    protected AbstractSpell getSelectedSpell(Player player) {
        // 나중에 슬롯 시스템에서 가져올 부분
        return GenesisSpells.FIREBALL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        AbstractSpell currentSpell = getSelectedSpell(player);

        //  시전 조건(스탯, 마나 등) 확인
        if (currentSpell.canCast(player)) {
            //  조건 만족 시 주문의 '시전 시작' 로직 호출
            // (여기서부터 차징, 모션 재생 등은 주문/스킬 시스템이 알아서 처리)
            if (!level.isClientSide) {
                currentSpell.execute(level, player);
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        } else {
            //  조건 미달 시: 불 꺼지는 소리 재생 (실패 피드백)
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
    }
}