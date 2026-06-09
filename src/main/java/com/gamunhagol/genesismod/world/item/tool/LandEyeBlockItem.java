package com.gamunhagol.genesismod.world.item.tool;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSpawnEyeBeam;
import com.gamunhagol.genesismod.world.structure.MistVaultTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class LandEyeBlockItem extends BlockItem {
    public LandEyeBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() != Level.OVERWORLD) {
                return InteractionResultHolder.success(itemstack);
            }

            MistVaultTracker tracker = MistVaultTracker.get(serverLevel);
            BlockPos playerPos = player.blockPosition();
            BlockPos nearestVault = tracker.findNearest(playerPos);

            if (nearestVault != null) {
                double dx = nearestVault.getX() - playerPos.getX();
                double dz = nearestVault.getZ() - playerPos.getZ();
                double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                double targetY;

                if (horizontalDistance < 100.0D) {
                    targetY = nearestVault.getY();
                } else {
                    targetY = player.getEyeY();
                }

                Vec3 finalTarget = new Vec3(nearestVault.getX() + 0.5D, targetY, nearestVault.getZ() + 0.5D);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);

                if (player instanceof ServerPlayer serverPlayer) {
                    GenesisNetwork.sendToPlayer(new PacketSpawnEyeBeam(finalTarget), serverPlayer);
                }

                player.getCooldowns().addCooldown(this, 120);
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
