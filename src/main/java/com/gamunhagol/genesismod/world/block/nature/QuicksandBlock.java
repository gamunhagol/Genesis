package com.gamunhagol.genesismod.world.block.nature;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuicksandBlock extends Block {

    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.9F, 1.0D);

    public QuicksandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (entity != null) {
                if (entity.fallDistance > 2.5F) {
                    return FALLING_COLLISION_SHAPE;
                }

                boolean isFallingBlock = entity instanceof FallingBlockEntity;
                boolean canWalk = isFallingBlock || entity instanceof Rabbit || canWalkOnQuicksand(entity);

                if (canWalk && context.isAbove(Shapes.block(), pos, false) && !entityContext.isDescending()) {
                    return super.getCollisionShape(state, level, pos, context);
                }
            }
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity living)) return;


        if (!level.isClientSide) {
            BlockPos eyePos = BlockPos.containing(living.getX(), living.getEyeY(), living.getZ());
            if (level.getBlockState(eyePos).is(this)) {

                int currentAir = living.getAirSupply();
                living.setAirSupply(currentAir - 1);

                if (living.getAirSupply() < -20) {
                    living.setAirSupply(0);
                    living.hurt(level.damageSources().drown(), 2.0F);
                }

                if (living.tickCount % 20 == 0) {
                    living.hurt(level.damageSources().inWall(), 1.0F);
                }
            }
        }
    }

    public static boolean canWalkOnQuicksand(Entity entity) {
        if (entity instanceof LivingEntity living) {
            ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
            return boots.getItem() == GenesisItems.SCORPION_BOOTS.get();
        }
        return false;
    }
}