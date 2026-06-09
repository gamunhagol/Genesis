package com.gamunhagol.genesismod.world.block.custom;

import com.gamunhagol.genesismod.world.block.entity.MistVaultBlockEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MistVault1Block extends BaseEntityBlock {
    public MistVault1Block(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MistVaultBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.is(GenesisItems.KEY_OF_OBLIVION.get())) {
            if (!level.isClientSide) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(GenesisItems.MISY_CORE_1.get()));

                level.destroyBlock(pos, false);

                heldItem.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            int side = random.nextInt(5);
            switch (side) {
                case 0 -> { // Top
                    x += random.nextDouble();
                    y += 0.9D;
                    z += random.nextDouble();
                }
                case 1 -> { // North
                    x += random.nextDouble();
                    y += random.nextDouble();
                    z += -0.05D;
                }
                case 2 -> { // South
                    x += random.nextDouble();
                    y += random.nextDouble();
                    z += 1.05D;
                }
                case 3 -> { // West
                    x += -0.05D;
                    y += random.nextDouble();
                    z += random.nextDouble();
                }
                case 4 -> { // East
                    x += 1.05D;
                    y += random.nextDouble();
                    z += random.nextDouble();
                }
            }

            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0D, -0.05D, 0.0D);
        }
    }
}