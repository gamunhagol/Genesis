package com.gamunhagol.genesismod.world.item.custom;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncSpellSlot;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellExtensionItem extends Item {

    public SpellExtensionItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 클라이언트가 아닌 서버사이드에서만 데이터 처리
        if (!level.isClientSide()) {
            // 플레이어의 마법 슬롯 Capability 가져오기
            player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(spellSlot -> {
                int currentMax = spellSlot.getMemoryCapacity();
                int absoluteMax = 20;

                if (currentMax < absoluteMax) {
                    spellSlot.setMemoryCapacity(currentMax + 1);

                    // 클라이언트로 변경된 슬롯 데이터 동기화 패킷 전송
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncSpellSlot(
                                    spellSlot.getMemoryCapacity(),
                                    spellSlot.getSelectedSlot(),
                                    spellSlot.getEquippedSpells()
                            ),
                            (ServerPlayer) player
                    );
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            });
        }

        return InteractionResultHolder.success(itemStack);
    }
}
