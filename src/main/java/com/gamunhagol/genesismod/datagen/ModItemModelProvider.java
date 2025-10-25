package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.google.common.hash.HashCode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModItemModelProvider extends ItemModelProvider {
    private final PackOutput output;

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GenesisMod.MODID, existingFileHelper);
        this.output = output;
    }

    @Override
    protected void registerModels() {
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

        wallItem(GenesisBlocks.FADED_BRICK_WALL, GenesisBlocks.FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_SLAB);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_SLAB);
        pillarItem(GenesisBlocks.FADED_PILLAR);

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

        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(GenesisItems.AMETHYST_NEEDLE);

        // 🔹 정령 나침반 시리즈 자동 생성
        String[] types = {"fire", "water", "earth", "storm", "lightning", "plants", "ice"};
        for (String type : types) {
            makeCompassSeries(type);
        }
    }

    // ─────────────────────────────
    //  일반 아이템 모델 생성 헬퍼들
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

    public void clusterItem(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
        withExistingParent(name, new ResourceLocation("item/generated"))
                .texture("layer0",
                        new ResourceLocation(GenesisMod.MODID, "block/" + name));
    }

    public void evenSimpleBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(
                GenesisMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(
                        ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                        mcLoc("block/wall_inventory"))
                .texture("wall",
                        new ResourceLocation(GenesisMod.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void pillarItem(RegistryObject<Block> block) {
        this.withExistingParent(
                ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    // ─────────────────────────────
    //  나침반 시리즈 + Epic Fight JSON
    // ─────────────────────────────
    private void makeCompassSeries(String type) {
        getBuilder("spirit_compass_" + type)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", "genesis:item/spirit_compass_" + type + "_00")
                .override()
                .predicate(rawAngle(), 0.0f)
                .model(new ModelFile.UncheckedModelFile("genesis:item/spirit_compass_" + type + "_00"))
                .end();

        for (int i = 0; i < 32; i++) {
            float angle = (float) i / 32.0f;
            String index = String.format("%02d", i);

            getBuilder("spirit_compass_" + type + "_" + index)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", "genesis:item/spirit_compass_" + type + "_" + index);

            getBuilder("spirit_compass_" + type)
                    .override()
                    .predicate(rawAngle(), angle)
                    .model(new ModelFile.UncheckedModelFile("genesis:item/spirit_compass_" + type + "_" + index))
                    .end();
        }

        createEpicFightJson(type);
    }

    /**
     * ✅ Epic Fight 렌더링 설정 JSON 자동 생성 (1.20.1 완전 호환)
     */
    private void createEpicFightJson(String type) {
        JsonObject json = new JsonObject();
        json.addProperty("force_vanilla_first_person", true);
        json.addProperty("alwaysInHand", false);
        json.addProperty("appeared_in_afterimage", true);

        JsonObject transforms = new JsonObject();
        JsonObject mainhand = new JsonObject();
        JsonObject toolR = new JsonObject();
        JsonArray translation = new JsonArray();
        translation.add(0.0f);
        translation.add(0.05f);
        translation.add(-0.12f);
        toolR.add("translation", translation);
        mainhand.add("Tool_R", toolR);
        transforms.add("mainhand", mainhand);
        json.add("transforms", transforms);

        Path path = this.output.getOutputFolder()
                .resolve("assets/epicfight/items/genesis/spirit_compass_" + type + ".json");

        try {
            // ✅ 직접 파일 저장 (Forge 1.20.1용 안전 방식)
            Files.createDirectories(path.getParent());
            DataProvider.saveStable(new DummyCache(), json, path);
            GenesisMod.LOGGER.info("[DataGen] ✅ Created EpicFight JSON for {}", type);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to generate EpicFight JSON for " + type, e);
        }
    }

    /**
     * ✅ Dummy CachedOutput 구현체 (Forge 1.20.1에서 NO_CACHE 대체)
     */
    private static class DummyCache implements CachedOutput {
        @Override
        public void writeIfNeeded(Path path, byte[] bytes, HashCode hashCode) throws IOException {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
        }
    }

    /**
     * ✅ "minecraft:angle" → "angle"로 강제 출력
     */
    private static ResourceLocation rawAngle() {
        return new ResourceLocation("genesis_temp:angle") {
            @Override
            public String toString() {
                return "angle";
            }
        };
    }
}
