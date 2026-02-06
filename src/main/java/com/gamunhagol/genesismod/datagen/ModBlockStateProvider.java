package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.*;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, GenesisMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(GenesisBlocks.SILVER_ORE);
        blockWithItem(GenesisBlocks.DEEPSLATE_SILVER_ORE);
        blockWithItem(GenesisBlocks.RAW_SILVER_BLOCK);
        blockWithItem(GenesisBlocks.SILVER_BLOCK);

        blockWithItem(GenesisBlocks.ELVENIA_BLOCK);
        blockWithItem(GenesisBlocks.ANCIENT_ELVENIA_BLOCK);


        blockWithItem(GenesisBlocks.PEWRIESE_ORE);
        blockWithItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK);

        blockWithItem(GenesisBlocks.PYULITELA_BLOCK);

        blockWithItem(GenesisBlocks.BLUE_CRYSTAL_BLOCK);
        blockWithItem(GenesisBlocks.CITRINE_BLOCK);
        blockWithItem(GenesisBlocks.RED_CRYSTAL_BLOCK);
        blockWithItem(GenesisBlocks.GREEN_AMBER_BLOCK);
        blockWithItem(GenesisBlocks.ICE_FLOWER_BLOCK);
        blockWithItem(GenesisBlocks.LIGHTING_CRYSTAL_BLOCK);
        blockWithItem(GenesisBlocks.WIND_STONE_BLOCK);

        snowLayerBlock(GenesisBlocks.COPPER_COIN_PILE);
        snowLayerBlock(GenesisBlocks.SILVER_COIN_PILE);
        snowLayerBlock(GenesisBlocks.GOLD_COIN_PILE);
        snowLayerBlock(GenesisBlocks.PLATINUM_COIN_PILE);

        blockWithItem(GenesisBlocks.FADED_STONE);
        blockWithItem(GenesisBlocks.FADED_BRICK);



        getVariantBuilder(GenesisBlocks.FADED_GATEWAY.get())
                .forAllStates(state -> {
                    int light = state.getValue(FadedGatewayBlock.LIGHT_LEVEL);
                    // 5 이하는 정지된 기본 모델, 그 외(15)는 애니메이션 모델 사용
                    String modelName = (light <= 5) ? "faded_gateway" : "enable_faded_gateway";
                    return ConfiguredModel.builder()
                            .modelFile(models().cubeAll(modelName, modLoc("block/" + modelName)))
                            .build();
                });
        // 아이템 모델 설정 (인벤토리에서는 항상 꺼진 상태인 faded_gateway 사용)
        simpleBlockItem(GenesisBlocks.FADED_GATEWAY.get(),
                models().cubeAll("faded_gateway", modLoc("block/faded_gateway")));

        getVariantBuilder(GenesisBlocks.FADED_CHEST.get())
                .forAllStates(state -> {
                    boolean isOpen = state.getValue(FadedChestBlock.OPEN);
                    String topTexture = isOpen ? "faded_chest_top_open" : "faded_chest_top";

                    return ConfiguredModel.builder()
                            .modelFile(models().cube("faded_chest_" + (isOpen ? "opened" : "closed"),
                                    modLoc("block/faded_stone"),
                                    modLoc("block/" + topTexture),
                                    modLoc("block/faded_chest_side"),
                                    modLoc("block/faded_chest_side"),
                                    modLoc("block/faded_chest_side"),
                                    modLoc("block/faded_chest_side")
                            ).texture("particle", modLoc("block/faded_chest_side")))
                            .build();
                });

        simpleBlock(GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get(),
                models().cubeAll("statue_of_sentinel_of_oblivion", modLoc("block/statue_of_sentinel_of_oblivion")));
        simpleBlock(GenesisBlocks.STATUE_OF_HERALD_OF_OBLIVION.get(),
                models().cubeAll("statue_of_herald_of_oblivion", modLoc("block/statue_of_herald_of_oblivion")));
        simpleBlock(GenesisBlocks.STATUE_OF_GUIDE_TO_OBLIVION.get(),
                models().cubeAll("statue_of_guide_to_oblivion", modLoc("block/statue_of_guide_to_oblivion")));

        simpleBlock(GenesisBlocks.AEK_STATUE.get(),
                models().cubeAll("ancient_elf_knight_statue", modLoc("block/ancient_elf_knight_statue")));

        getVariantBuilder(GenesisBlocks.AMETHYST_APPLE_BLOCK.get())
                .partialState().with(AmethystAppleBlock.HANGING, false)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/amethyst_apple_ground"))).addModel()
                .partialState().with(AmethystAppleBlock.HANGING, true)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/amethyst_apple_hanging"))).addModel();

        getVariantBuilder(GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get())
                .forAllStates(state -> {
                    int portions = state.getValue(AmethystApplePuddingBlock.PORTIONS);
                    String modelName;

                    switch (portions) {
                        case 0 -> modelName = "pudding_full";
                        case 1 -> modelName = "pudding_stage1";
                        case 2 -> modelName = "pudding_stage2";
                        case 3 -> modelName = "pudding_stage3";
                        case 4 -> modelName = "pudding_empty";
                        default -> modelName = "pudding_full";
                    }

                    // 방향(FACING)에 따른 모델 회전 적용
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                            .rotationY((int) state.getValue(AmethystApplePuddingBlock.FACING).toYRot())
                            .build();
                });




        simpleBlock(GenesisBlocks.CHISELED_FADED_BRICK.get(),
                models().cubeColumn(
                        "chiseled_faded_brick", // 모델 파일 이름
                        blockTexture(GenesisBlocks.CHISELED_FADED_BRICK.get()), // 옆면 텍스처 (기본)
                        new ResourceLocation(GenesisMod.MODID, "block/chiseled_faded_brick_top") // 윗면/아랫면 텍스처
                )
        );

        stairsBlock(((StairBlock) GenesisBlocks.FADED_STONE_STAIRS.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        stairsBlock(((StairBlock) GenesisBlocks.FADED_BRICK_STAIRS.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        slabBlock(((SlabBlock) GenesisBlocks.FADED_STONE_SLAB.get()), blockTexture(GenesisBlocks.FADED_STONE.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        slabBlock(((SlabBlock) GenesisBlocks.FADED_BRICK_SLAB.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        wallBlock(((WallBlock) GenesisBlocks.FADED_BRICK_WALL.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        axisBlock(((RotatedPillarBlock) GenesisBlocks.FADED_PILLAR.get()), blockTexture(GenesisBlocks.FADED_PILLAR.get()),
                new ResourceLocation(GenesisMod.MODID, "block/faded_pillar_top"));


        clusterBlock(GenesisBlocks.BLUE_CRYSTAL_CLUSTER);
        clusterBlock(GenesisBlocks.CITRINE_CLUSTER);
        clusterBlock(GenesisBlocks.RED_CRYSTAL_CLUSTER);
        clusterBlock(GenesisBlocks.GREEN_AMBER_CLUSTER);
        clusterBlock(GenesisBlocks.ICE_FLOWER_CLUSTER);
        clusterBlock(GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER);
        clusterBlock(GenesisBlocks.WIND_STONE_CLUSTER);

        customCandleBlock(GenesisBlocks.OBLIVION_CANDLE);






    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void clusterBlock(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();

        var model = models().cross(name, modLoc("block/" + name))
                .renderType("cutout");

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);

            int xRot = switch (dir) {
                case DOWN -> 180;
                case UP -> 0;
                default -> 90;
            };

            int yRot = switch (dir) {
                case NORTH -> 0;
                case SOUTH -> 180;
                case WEST -> 270;
                case EAST -> 90;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });
    }

    private void clusterBlockSet(
            RegistryObject<Block> small,
            RegistryObject<Block> medium,
            RegistryObject<Block> large,
            RegistryObject<Block> cluster
    )
    {
        clusterBlock(small);
        clusterBlock(medium);
        clusterBlock(large);
        clusterBlock(cluster);
    }

    private void snowLayerBlock(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();

        getVariantBuilder(block.get()).forAllStates(state -> {
            int layers = state.getValue(BlockStateProperties.LAYERS);

            if (layers == 8) {
                return ConfiguredModel.builder()
                        .modelFile(models().cubeAll(name + "_all", modLoc("block/" + name)))
                        .build();
            } else {
                // 바닐라 눈 모델을 부모로 빌려와서 층별 높이 조절 (블록 전용)
                return ConfiguredModel.builder()
                        .modelFile(models().withExistingParent(name + "_height" + layers, mcLoc("block/snow_height" + (layers * 2)))
                                .texture("texture", modLoc("block/" + name))
                                .texture("particle", modLoc("block/" + name)))
                        .build();
            }
        });
        this.itemModels().withExistingParent(name, new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(GenesisMod.MODID, "item/" + name));
    }

    private void customCandleBlock(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();

        ResourceLocation candleTexture = modLoc("block/oblivion_candle_lit");

        var candle1 = models().withExistingParent(name + "_one", mcLoc("block/template_candle"))
                .texture("all", candleTexture)
                .texture("particle", candleTexture)
                .renderType("cutout");

        var candle2 = models().withExistingParent(name + "_two", mcLoc("block/template_two_candles"))
                .texture("all", candleTexture)
                .texture("particle", candleTexture)
                .renderType("cutout");

        var candle3 = models().withExistingParent(name + "_three", mcLoc("block/template_three_candles"))
                .texture("all", candleTexture)
                .texture("particle", candleTexture)
                .renderType("cutout");

        var candle4 = models().withExistingParent(name + "_four", mcLoc("block/template_four_candles"))
                .texture("all", candleTexture)
                .texture("particle", candleTexture)
                .renderType("cutout");

        getVariantBuilder(block.get()).forAllStates(state -> {
            int candles = state.getValue(CandleBlock.CANDLES);
            return ConfiguredModel.builder()
                    .modelFile(candles == 1 ? candle1 : candles == 2 ? candle2 : candles == 3 ? candle3 : candle4)
                    .build();
        });
    }
}
