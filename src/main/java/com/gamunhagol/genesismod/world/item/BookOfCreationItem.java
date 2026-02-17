package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BookOfCreationItem extends Item {
    public BookOfCreationItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        //  서버 사이드에서만 데이터를 처리하여 튕김 방지 [cite: 2026-02-15]
        if (!level.isClientSide()) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                if (!stats.isLevelUpUnlocked()) {
                    stats.setLevelUpUnlocked(true);

                    // 효과음 재생
                    level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    // ★ 중요: 데이터가 바뀌었음을 알려 다음 틱에서 바로 PacketSyncStats가 날아가게 함
                    stats.setDirty(true);
                }
            });
        }
        //  클라이언트와 서버 모두에게 성공 알림 [cite: 2026-02-15]
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}