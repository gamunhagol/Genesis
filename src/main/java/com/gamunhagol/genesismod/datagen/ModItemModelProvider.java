package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterial = new LinkedHashMap<>();
    static {
        trimMaterial.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterial.put(TrimMaterials.IRON, 0.2F);
        trimMaterial.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterial.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterial.put(TrimMaterials.COPPER, 0.5F);
        trimMaterial.put(TrimMaterials.GOLD, 0.6F);
        trimMaterial.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterial.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterial.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterial.put(TrimMaterials.AMETHYST, 1.0F);
    }


    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, GenesisMod.MODID, helper);
    }

    @Override
    protected void registerModels() {
        // ─────────────── 일반 아이템 ───────────────
        simpleItem(GenesisItems.BOOK_OF_CREATION);

        simpleItem(GenesisItems.SCATTERED_MEMORIES);
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
        simpleItem(GenesisItems.ICE_FLOWER_SHARD);

        clusterItem(GenesisBlocks.BLUE_CRYSTAL_CLUSTER);
        clusterItem(GenesisBlocks.CITRINE_CLUSTER);
        clusterItem(GenesisBlocks.RED_CRYSTAL_CLUSTER);
        clusterItem(GenesisBlocks.GREEN_AMBER_CLUSTER);
        clusterItem(GenesisBlocks.ICE_FLOWER_CLUSTER);
        clusterItem(GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER);
        clusterItem(GenesisBlocks.WIND_STONE_CLUSTER);

        clusterItem(GenesisBlocks.AMETHYST_SAPLING);

        simpleItem(GenesisItems.RAW_SILVER);
        simpleItem(GenesisItems.SILVER_PIECE);
        simpleItem(GenesisItems.SILVER_INGOT);

        simpleItem(GenesisItems.ELVENIA_PIECE);
        simpleItem(GenesisItems.ELVENIA_INGOT);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_PIECE);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_INGOT);



        simpleItem(GenesisItems.PEWRIESE_ORE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_CRYSTAL);
        simpleItem(GenesisItems.PYULITELA);

        simpleItem(GenesisItems.BEAST_REMAINS);
        simpleItem(GenesisItems.FLASK_SHARD);

        simpleItem(GenesisItems.COPPER_COIN);
        simpleItem(GenesisItems.SILVER_COIN);
        simpleItem(GenesisItems.GOLD_COIN);
        simpleItem(GenesisItems.PLATINUM_COIN);

        simpleItem(GenesisItems.AMETHYST_APPLE);

        simpleItem(GenesisItems.AMETHYST_APPLE_SLICES);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING_BLOCK);

        simpleItem(GenesisItems.OBLIVION_CANDLE);

        handheldItem(GenesisItems.ELVENIA_SWORD);
        handheldItem(GenesisItems.ELVENIA_AXE);
        handheldItem(GenesisItems.ELVENIA_PICKAXE);
        handheldItem(GenesisItems.ELVENIA_HOE);
        handheldItem(GenesisItems.ELVENIA_SHOVEL);

        handheldItem(GenesisItems.ANCIENT_ELVENIA_SWORD);
        handheldItem(GenesisItems.ANCIENT_ELVENIA_AXE);
        handheldItem(GenesisItems.ANCIENT_ELVENIA_PICKAXE);
        handheldItem(GenesisItems.ANCIENT_ELVENIA_HOE);
        handheldItem(GenesisItems.ANCIENT_ELVENIA_SHOVEL);

        handheldItem(GenesisItems.PEWRIESE_SWORD);
        handheldItem(GenesisItems.PEWRIESE_AXE);
        handheldItem(GenesisItems.PEWRIESE_PICKAXE);
        handheldItem(GenesisItems.PEWRIESE_HOE);
        handheldItem(GenesisItems.PEWRIESE_SHOVEL);

        simpleItem(GenesisItems.AMETHYST_NEEDLE);
        simpleItem(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);

        simpleItem(GenesisItems.MEDALLION_OF_DOMINION);

        simpleItem(GenesisItems.HOT_SPRING_BUCKET);
        simpleItem(GenesisItems.QUICKSAND_BUCKET);

        simpleItem(GenesisItems.COLLECTOR_SPAWN_EGG);
        simpleItem(GenesisItems.COLLECTOR_GUARD_SPAWN_EGG);

        simpleItem(GenesisItems.STATUE_OF_SENTINEL_OF_OBLIVION);
        simpleItem(GenesisItems.STATUE_OF_HERALD_OF_OBLIVION);
        simpleItem(GenesisItems.STATUE_OF_GUIDE_TO_OBLIVION);
        simpleItem(GenesisItems.AEK_STATUE);

        basicItemModel("divine_grail_empty");
        basicItemModel("divine_grail_near_empty");
        basicItemModel("divine_grail_half");
        basicItemModel("divine_grail_most");
        basicItemModel("divine_grail_full");

        ItemModelBuilder grailBuilder = withExistingParent(GenesisItems.DIVINE_GRAIL.getId().getPath(),
                new ResourceLocation("item/handheld"))
                .texture("layer0", new ResourceLocation(GenesisMod.MODID, "item/divine_grail_full"));



        // ─────────────── 블록 아이템 ───────────────
        wallItem(GenesisBlocks.FADED_BRICK_WALL, GenesisBlocks.FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.CHISELED_FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_SLAB);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_SLAB);
        pillarItem(GenesisBlocks.FADED_PILLAR);

        this.withExistingParent("faded_chest", modLoc("block/faded_chest_closed"));




        // ─────────────── 방어구 ───────────────
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_HELMET);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_CHESTPLATE);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_LEGGINGS);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_BOOTS);

        simpleItem(GenesisItems.ELVENIA_HELMET);
        simpleItem(GenesisItems.ELVENIA_CHESTPLATE);
        simpleItem(GenesisItems.ELVENIA_LEGGINGS);
        simpleItem(GenesisItems.ELVENIA_BOOTS);

        simpleItem(GenesisItems.ANCIENT_ELVENIA_HELMET);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_CHESTPLATE);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_LEGGINGS);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_BOOTS);

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
        withExistingParent(name, new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(GenesisMod.MODID, "item/" + name));
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



    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = GenesisMod.MODID;

        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            // 1. 기본 아이템 모델을 루프 '밖'에서 먼저 만듭니다.
            String itemName = itemRegistryObject.getId().getPath();
            ItemModelBuilder builder = this.withExistingParent(itemName,
                            new ResourceLocation("minecraft", "item/generated"))
                    .texture("layer0", new ResourceLocation(MOD_ID, "item/" + itemName));

            // 2. 루프를 돌며 트리밍 모델을 만들고, 기본 모델에 오버라이드를 추가합니다.
            trimMaterial.entrySet().forEach(entry -> {
                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                // 트리밍된 모델 파일 생성 (item/padded_chain_helmet_quartz_trim 등)
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = itemName + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, "item/" + itemName);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft:trims/...
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // 텍스처 존재 여부 확인 (Client Resources)
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // 트리밍 전용 모델 파일 생성
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // [핵심] 1번에서 만든 builder에 오버라이드만 쏙쏙 추가
                builder.override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue)
                        .end();
            });
        }
    }

    private void addGrailOverrides(ItemModelBuilder builder) {
        // 0.0: 텅 빈 상태
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.0f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_empty")))
                .end();
        // 0.25: 거의 다 마심
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.25f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_near_empty")))
                .end();
        // 0.5: 절반
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.5f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_half")))
                .end();
        // 0.75: 조금 마심
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.75f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_most")))
                .end();
        // 1.0: 꽉 참
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 1.0f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_full")))
                .end();
    }
}
