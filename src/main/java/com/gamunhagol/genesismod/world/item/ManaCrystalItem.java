package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncMentalPower;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaCrystalItem extends Item {
    public ManaCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                float recoveryAmount = 5.0f;
                stats.setMental(Math.min(stats.getMaxMental(), stats.getMental() + recoveryAmount));

                // [★버그 수정] 값이 변했으니 클라이언트에게 즉시 "야! 값 바뀌었어!" 하고 소리쳐야 합니다.
                if (player instanceof ServerPlayer serverPlayer) {
                    GenesisNetwork.sendToPlayer(new PacketSyncMentalPower(stats.getMental()), serverPlayer);
                }
            });
            //  효과음 재생
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            //  파티클 소환 (서버 월드인 경우)
            if (level instanceof ServerLevel serverLevel) {
                // (A) 자수정 파편 효과: 아이템이 부서지는 입자
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemstack),
                        player.getX(), player.getY() + 1.2D, player.getZ(),
                        10, 0.2D, 0.2D, 0.2D, 0.1D);
                // (B) 파랑 파티클 (물방울/마력 기운): 찔끔 올라가는 연출
                // ParticleTypes.BUBBLE이나 DRIPPING_WATER 등을 섞으면 '물' 느낌이 납니다.
                serverLevel.sendParticles(ParticleTypes.GLOW,
                        player.getX(), player.getY() + 1.0D, player.getZ(),
                        5, 0.3D, 0.5D, 0.3D, 0.05D);
            }
        }
        //소모
        player.swing(hand, true);
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}