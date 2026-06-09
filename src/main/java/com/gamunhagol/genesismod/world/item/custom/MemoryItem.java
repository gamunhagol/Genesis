package com.gamunhagol.genesismod.world.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MemoryItem extends Item {

    private final int levelsToGive;

    public MemoryItem(Properties properties, int levelsToGive) {
        super(properties);
        this.levelsToGive = levelsToGive;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // (경험치 포인트가 아닌 '레벨'을 올리려면 이 메서드를 사용해야 합니다)
            player.giveExperienceLevels(this.levelsToGive);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}