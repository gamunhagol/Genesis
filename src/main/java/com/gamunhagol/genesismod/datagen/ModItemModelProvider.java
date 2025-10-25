package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, GenesisMod.MODID, helper);
    }

    @Override
    protected void registerModels() {
        // ─────────────── 일반 아이템 ───────────────
        simpleItem(GenesisItems.BOOK_OF_CREATION);

        simpleItem(GenesisItems.DREAM_POWDER);
        simpleItem(GenesisItems.DREAM_DANGO);
        simpleItem(GenesisItems.REMNANTS_OF_A_DREAM);
        basicItem(GenesisItems.FRAGMENT_OF_MEMORY.get());

        simpleItem(GenesisItems.BLUE_CRYSTAL_SHARD);
        simpleItem(GenesisItems.CITRINE_SHARD);
        simpleItem(GenesisItems.RED_CRYSTAL_SHARD);
        simpleItem(GenesisItems.WIND_STONE);
        simpleItem(GenesisItems.LIGHTING_CRYSTAL_SHARD);
        simpleItem(GenesisItems.GREEN_AMBER);
        simpleItem(GenesisItems.FROST_CRYSTAL_SHARD);
        simpleItem(GenesisItems.FADED_CRYSTAL_SHARD);

        clusterItem(GenesisBlocks.BLUE_CRYSTAL_CLUSTER);
        clusterItem(GenesisBlocks.CITRINE_CLUSTER);
        clusterItem(GenesisBlocks.RED_CRYSTAL_CLUSTER);

        simpleItem(GenesisItems.PEWRIESE_ORE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_CRYSTAL);
        simpleItem(GenesisItems.PYULITELA);

        simpleItem(GenesisItems.COPPER_COIN);
        simpleItem(GenesisItems.SILVER_COIN);
        simpleItem(GenesisItems.GOLD_COIN);
        simpleItem(GenesisItems.PLATINUM_COIN);

        handheldItem(GenesisItems.PEWRIESE_SWORD);
        handheldItem(GenesisItems.PEWRIESE_AXE);
        handheldItem(GenesisItems.PEWRIESE_PICKAXE);
        handheldItem(GenesisItems.PEWRIESE_HOE);
        handheldItem(GenesisItems.PEWRIESE_SHOVEL);

        simpleItem(GenesisItems.AMETHYST_NEEDLE);
        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);

        // ─────────────── 블록 아이템 ───────────────
        wallItem(GenesisBlocks.FADED_BRICK_WALL, GenesisBlocks.FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_SLAB);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_SLAB);
        pillarItem(GenesisBlocks.FADED_PILLAR);

        // ─────────────── 방어구 ───────────────
        simpleItem(GenesisItems.PEWRIESE_HELMET);
        simpleItem(GenesisItems.PEWRIESE_CHESTPLATE);
        simpleItem(GenesisItems.PEWRIESE_LEGGINGS);
        simpleItem(GenesisItems.PEWRIESE_BOOTS);

        simpleItem(GenesisItems.PEWRIESE_PLATE_HELMET);
        simpleItem(GenesisItems.PEWRIESE_PLATE_CHESTPLATE);
        simpleItem(GenesisItems.PEWRIESE_PLATE_LEGGINGS);
        simpleItem(GenesisItems.PEWRIESE_PLATE_BOOTS);

        simpleItem(GenesisItems.HOLY_KNIGHT_HELMET);
        simpleItem(GenesisItems.HOLY_KNIGHT_CHESTPLATE);
        simpleItem(GenesisItems.HOLY_KNIGHT_LEGGINGS);
        simpleItem(GenesisItems.HOLY_KNIGHT_BOOTS);

        // ─────────────── 정령 나침반 시리즈 ───────────────
        String[] types = {"fire", "water", "earth", "storm", "lightning", "plants", "ice"};
        for (String type : types) {
            basicItemModel("spirit_compass_" + type);
        }

    }

    // ─────────────────────────────
    //  아이템 모델 생성 헬퍼
    // ─────────────────────────────

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated"))
                .texture("layer0",
                        new ResourceLocation(GenesisMod.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld"))
                .texture("layer0",
                        new ResourceLocation(GenesisMod.MODID, "item/" + item.getId().getPath()));
    }

    private void basicItemModel(String name) {
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", "genesis:item/" + name);
    }
    // ─────────────────────────────
    //  오버라이드 추가 헬퍼 메서드
    // ─────────────────────────────

    private void addCompassOverride(ItemModelBuilder builder, float value, String type) {
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "needle_type"), value)
                .model(new ModelFile.UncheckedModelFile("genesis:item/spirit_compass_" + type))
                .end();
    }




    // ─────────────────────────────
    //  블록 아이템용 헬퍼
    // ─────────────────────────────
    public void clusterItem(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
        withExistingParent(name, new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(GenesisMod.MODID, "block/" + name));

    }

    public void evenSimpleBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(
                GenesisMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void pillarItem(RegistryObject<Block> block) {
        this.withExistingParent(
                ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(
                        ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                        mcLoc("block/wall_inventory"))
                .texture("wall",
                        new ResourceLocation(GenesisMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }
}
