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

    public static final RegistryObject<BlockEntityType<StatueBlockEntity.Sentinel>> SENTINEL_STATUE_BE =
            BLOCK_ENTITIES.register("sentinel_statue_be", () ->
                    BlockEntityType.Builder.of(StatueBlockEntity.Sentinel::new,
                            GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get()).build(null));
    public static final RegistryObject<BlockEntityType<StatueBlockEntity.Herald>> HERALD_STATUE_BE =
            BLOCK_ENTITIES.register("herald_statue_be", () ->
                    BlockEntityType.Builder.of(StatueBlockEntity.Herald::new,
                            GenesisBlocks.STATUE_OF_HERALD_OF_OBLIVION.get()).build(null));
    public static final RegistryObject<BlockEntityType<StatueBlockEntity.Guide>> GUIDE_STATUE_BE =
            BLOCK_ENTITIES.register("guide_statue_be", () ->
                    BlockEntityType.Builder.of(StatueBlockEntity.Guide::new,
                            GenesisBlocks.STATUE_OF_GUIDE_TO_OBLIVION.get()).build(null));


    public static final RegistryObject<BlockEntityType<AEKStatueBlockEntity>> AEK_STATUE_BE =
            BLOCK_ENTITIES.register("aek_statue_be", () ->
                    BlockEntityType.Builder.of(AEKStatueBlockEntity::new,
                            GenesisBlocks.AEK_STATUE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MistVaultBlockEntity>> MIST_VAULT_BE =
            BLOCK_ENTITIES.register("mist_vault_be", () ->
                    BlockEntityType.Builder.of(MistVaultBlockEntity::new,
                            GenesisBlocks.MIST_VAULT_1.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}