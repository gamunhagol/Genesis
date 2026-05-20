package com.gamunhagol.genesismod.world.capability.spell;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellBookItem extends Item {
    private final String spellId;

    public SpellBookItem(String spellId, Properties properties) {
        super(properties);
        this.spellId = spellId;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                // 이미 배운 마법인지 체크하고 등록
                if (!stats.hasSpell(this.spellId)) {
                    stats.learnSpell(this.spellId);
                    // 성공적으로 배웠을 때 인챈트 테이블 소리 재생
                    level.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
                            1.0F, 1.0F
                    );
                    // 사용 후 아이템 소모 (크리에이티브 모드가 아닐 때만)
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            });
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}