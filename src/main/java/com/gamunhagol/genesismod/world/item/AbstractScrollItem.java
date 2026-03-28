package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncMentalPower;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractScrollItem extends Item {
    protected final float fpCost;
    protected final int cooldownTicks;

    public AbstractScrollItem(Properties properties, float fpCost, int cooldownTicks) {
        super(properties.stacksTo(1));
        this.fpCost = fpCost;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemstack);
        }

        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            boolean success = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).map(stats -> {
                if (stats.getMental() >= fpCost) {
                    stats.setMental(stats.getMental() - fpCost);
                    GenesisNetwork.sendToPlayer(new PacketSyncMentalPower(stats.getMental()), serverPlayer);
                    return true;
                }
                return false;
            }).orElse(false);

            if (success) {
                // 1. 사운드 재생 (책장 넘기는 소리)
                // 첫 번째 인자가 null이면 서버가 주변 모든 플레이어에게 소리를 보냅니다.
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.VILLAGER_WORK_LIBRARIAN, SoundSource.PLAYERS, 1.0F, 1.0F);

                // 2. 효과 실행
                this.executeEffect(level, serverPlayer);

                // 3. 공통 처리 (쿨타임, 스윙, 아이템 소모)
                player.getCooldowns().addCooldown(this, cooldownTicks);
                player.swing(hand, true);

                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResultHolder.sidedSuccess(itemstack, false);
            }
        }
        return InteractionResultHolder.pass(itemstack);
    }

    protected abstract void executeEffect(Level level, ServerPlayer player);
}