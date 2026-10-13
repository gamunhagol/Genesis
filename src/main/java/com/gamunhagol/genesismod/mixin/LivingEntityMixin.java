package com.gamunhagol.genesismod.mixin;

import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState genesis$treatIceAsNormalBlock(Level level, BlockPos pos) {
        BlockState originalState = level.getBlockState(pos);
        LivingEntity self = (LivingEntity) (Object) this;

        if (!self.isPassenger() && originalState.is(BlockTags.ICE)) {
            if (self.hasEffect(GenesisEffects.STAND_ON_ICE.get())) {
                return Blocks.STONE.defaultBlockState();
            }
        }

        return originalState;
    }
}