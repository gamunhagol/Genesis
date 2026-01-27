package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GenesisMod.MODID);

    public static final RegistryObject<BlockEntityType<FadedChestBlockEntity>> FADED_CHEST_BE =
            BLOCK_ENTITIES.register("faded_chest_be", () ->
                    BlockEntityType.Builder.of(FadedChestBlockEntity::new,
                            GenesisBlocks.FADED_CHEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<AEKStatueBlockEntity>> AEK_STATUE_BE =
            BLOCK_ENTITIES.register("aek_statue_be", () ->
                    BlockEntityType.Builder.of(AEKStatueBlockEntity::new,
                            GenesisBlocks.AEK_STATUE.get()).build(null));


    // 메인 클래스(GenesisMod)에서 호출할 등록 메서드
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}