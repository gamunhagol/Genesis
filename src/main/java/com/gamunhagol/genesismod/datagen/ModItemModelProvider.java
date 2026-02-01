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

        simpleItem(GenesisItems.RAW_SILVER);
        simpleItem(GenesisItems.SILVER_PIECE);
        simpleItem(GenesisItems.SILVER_INGOT);

        simpleItem(GenesisItems.PEWRIESE_ORE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_CRYSTAL);
        simpleItem(GenesisItems.PYULITELA);

        simpleItem(GenesisItems.COPPER_COIN);
        simpleItem(GenesisItems.SILVER_COIN);
        simpleItem(GenesisItems.GOLD_COIN);
        simpleItem(GenesisItems.PLATINUM_COIN);

        simpleItem(GenesisItems.AMETHYST_APPLE);

        simpleItem(GenesisItems.AMETHYST_APPLE_SLICES);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING_BLOCK);




        handheldItem(GenesisItems.PEWRIESE_SWORD);
        handheldItem(GenesisItems.PEWRIESE_AXE);
        handheldItem(GenesisItems.PEWRIESE_PICKAXE);
        handheldItem(GenesisItems.PEWRIESE_HOE);
        handheldItem(GenesisItems.PEWRIESE_SHOVEL);

        simpleItem(GenesisItems.AMETHYST_NEEDLE);
        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);

        simpleItem(GenesisItems.MEDALLION_OF_DOMINION);

        simpleItem(GenesisItems.HOT_SPRING_BUCKET);
        simpleItem(GenesisItems.QUICKSAND_BUCKET);

        simpleItem(GenesisItems.COLLECTOR_SPAWN_EGG);
        simpleItem(GenesisItems.COLLECTOR_GUARD_SPAWN_EGG);

        simpleItem(GenesisItems.STATUE_OF_SENTINEL_OF_OBLIVION);


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
}
