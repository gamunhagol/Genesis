package com.gamunhagol.genesismod.datagen.models;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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
        // item
        simpleItem(GenesisItems.BOOK_OF_CREATION);

        simpleItem(GenesisItems.SCATTERED_MEMORIES);
        simpleItem(GenesisItems.DREAM_POWDER);
        simpleItem(GenesisItems.DREAM_DANGO);
        simpleItem(GenesisItems.REMNANTS_OF_A_DREAM);
        basicItem(GenesisItems.FRAGMENT_OF_MEMORY.get());

        basicItem(GenesisItems.MANA_IMBUED_AMETHYST_SHARD.get());

        simpleItem(GenesisItems.STAR_FRAGMENT);
        simpleItem(GenesisItems.AMETHYST_MAGIC_CORE);

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

        simpleItem(GenesisItems.CARBONIZED_INGOT);
        simpleItem(GenesisItems.GIANT_STONE_FRAGMENT);
        simpleItem(GenesisItems.FUSION_STONE);

        simpleItem(GenesisItems.WHITE_IRON_INGOT);
        simpleItem(GenesisItems.BASED_SCULPTURE);

        simpleItem(GenesisItems.COPPER_COIN);
        simpleItem(GenesisItems.SILVER_COIN);
        simpleItem(GenesisItems.GOLD_COIN);
        simpleItem(GenesisItems.PLATINUM_COIN);



        simpleItem(GenesisItems.LUMINOUS_INSECT_JUICE);
        simpleItem(GenesisItems.SCORPION_MEAT);
        simpleItem(GenesisItems.COOKED_SCORPION_MEAT);
        simpleItem(GenesisItems.ENCHANTED_GLOWING_HEART);

        simpleItem(GenesisItems.AMETHYST_APPLE);

        simpleItem(GenesisItems.AMETHYST_APPLE_SLICES);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING);
        simpleItem(GenesisItems.AMETHYST_APPLE_PUDDING_BLOCK);

        simpleItem(GenesisItems.BLOOD_BOTTLE);

        simpleItem(GenesisItems.OPAQUE_JELLY);

        simpleItem(GenesisItems.FADED_MEMORY);
        simpleItem(GenesisItems.FORGOTTEN_MEMORY);
        simpleItem(GenesisItems.UNRELATED_MEMORY);
        simpleItem(GenesisItems.OBLIVION_SPHERE);

        simpleItem(GenesisItems.OBLIVION_CANDLE);

        simpleItem(GenesisItems.FIREBALL);
        simpleItem(GenesisItems.LITTLE_HEAL);

        handheldItem(GenesisItems.HARDENED_GLASS_SWORD);
        handheldItem(GenesisItems.HARDENED_RED_GLASS_SWORD);

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

        handheldItem(GenesisItems.CARBONIZED_SWORD);
        handheldItem(GenesisItems.CARBONIZED_AXE);
        handheldItem(GenesisItems.CARBONIZED_PICKAXE);
        handheldItem(GenesisItems.CARBONIZED_HOE);
        handheldItem(GenesisItems.CARBONIZED_SHOVEL);

        handheldItem(GenesisItems.PEWRIESE_SWORD);
        handheldItem(GenesisItems.PEWRIESE_AXE);
        handheldItem(GenesisItems.PEWRIESE_PICKAXE);
        handheldItem(GenesisItems.PEWRIESE_HOE);
        handheldItem(GenesisItems.PEWRIESE_SHOVEL);

        handheldItem(GenesisItems.PURTRUCTION_SWORD);
        handheldItem(GenesisItems.PURTRUCTION_AXE);
        handheldItem(GenesisItems.PURTRUCTION_PICKAXE);
        handheldItem(GenesisItems.PURTRUCTION_HOE);
        handheldItem(GenesisItems.PURTRUCTION_SHOVEL);

        handheldItem(GenesisItems.HOLY_KNIGHT_SWORD);

        handheldItem(GenesisItems.PYULITELA_SWORD);
        handheldItem(GenesisItems.PYULITELA_AXE);
        handheldItem(GenesisItems.PYULITELA_PICKAXE);
        handheldItem(GenesisItems.PYULITELA_HOE);
        handheldItem(GenesisItems.PYULITELA_SHOVEL);


        handheldItem(GenesisItems.DEPTHS_SWORD);

        handheldItem(GenesisItems.OATH_IN_DEEP_DARK);

        simpleItem(GenesisItems.AMETHYST_HUMAN_STATUE);

        simpleItem(GenesisItems.LAGER_DESERT_SCORPION_TAIL);
        simpleItem(GenesisItems.LAGER_DESERT_SCORPION_PINCERS);

        simpleItem(GenesisItems.SCORPION_CARAPACE);
        simpleItem(GenesisItems.SACRED_STONE);

        simpleItem(GenesisItems.CLOTH);

        simpleItem(GenesisItems.ENCHANTED_CLOTH);
        simpleItem(GenesisItems.BLESSED_CLOTH);

        simpleItem(GenesisItems.METAL_FIBER);

        simpleItem(GenesisItems.RED_EYE_SMALL);
        simpleItem(GenesisItems.RED_EYE_MEDIUM);
        simpleItem(GenesisItems.RED_EYE_LARGE);

        simpleItem(GenesisItems.HARDENED_GLASS_PIECES);
        simpleItem(GenesisItems.HARDENED_RED_GLASS_PIECES);
        simpleItem(GenesisItems.HARDENED_RED_MASS);

        simpleItem(GenesisItems.ECHOING_SOUL);
        simpleItem(GenesisItems.SOUL_PUS);

        simpleItem(GenesisItems.AMETHYST_NEEDLE);
        simpleItem(GenesisItems.CRYSTAL_BAT_HIDE);

        simpleItem(GenesisItems.AMETHYST_BONE);
        simpleItem(GenesisItems.AMETHYST_HEART_PIECE);
        simpleItem(GenesisItems.AMETHYST_SHIELD_SHARD);

        simpleItem(GenesisItems.SMALL_BELL_OF_OBLIVION);

        simpleItem(GenesisItems.FOG_GUARDIAN_SHARD);

        simpleItem(GenesisItems.ILLUSION_SILK);

        simpleItem(GenesisItems.UNDERGROUND_BONE);

        simpleItem(GenesisItems.UNDEAD_REMNANT);

        simpleItem(GenesisItems.UNFINISHED_SHIELD);

        simpleItem(GenesisItems.SKULLK_SPROUT);

        simpleItem(GenesisItems.CRYSTALS_OF_THE_LAND);

        simpleItem(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);

        simpleItem(GenesisItems.SCALE_FOSSIL_SHARD);
        simpleItem(GenesisItems.SCALE_FOSSIL);
        simpleItem(GenesisItems.SCALE_FOSSIL_CLUMP);
        simpleItem(GenesisItems.ANCIENT_DRAGON_SCALE);
        simpleItem(GenesisItems.DRAGON_KING_SCALE);

        simpleItem(GenesisItems.SHARD_OF_THE_MOUNTAIN);
        simpleItem(GenesisItems.FRAGMENT_OF_THE_MOUNTAIN);
        simpleItem(GenesisItems.CLUMP_OF_THE_MOUNTAIN);
        simpleItem(GenesisItems.TABLET_SHARD);
        simpleItem(GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN);

        simpleItem(GenesisItems.LARGE_ARROW);

        simpleItem(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT);

        basicItem(GenesisItems.KEY_OF_OBLIVION.get());

        simpleItem(GenesisItems.MISY_CORE_1);

        simpleItem(GenesisItems.BASE_SPELL);

        simpleItem(GenesisItems.MEDALLION_OF_DOMINION);

        simpleItem(GenesisItems.FABRICATED_STAR);
        simpleItem(GenesisItems.CELESTIAL_STAR);
        simpleItem(GenesisItems.EPONYMOUS_STAR);



        basicItem(GenesisItems.LITTLE_STAR_OF_CLARITY.get());

        simpleItem(GenesisItems.HEAL_SCROLL_1);

        simpleItem(GenesisItems.QUICKSAND_BUCKET);
        simpleItem(GenesisItems.BLOOD_BUCKET);

        simpleItem(GenesisItems.COLLECTOR_SPAWN_EGG);
        simpleItem(GenesisItems.COLLECTOR_GUARD_SPAWN_EGG);

        simpleItem(GenesisItems.AMETHYST_HEART);

        simpleItem(GenesisItems.STATUE_OF_SENTINEL_OF_OBLIVION);
        simpleItem(GenesisItems.STATUE_OF_HERALD_OF_OBLIVION);
        simpleItem(GenesisItems.STATUE_OF_GUIDE_TO_OBLIVION);
        simpleItem(GenesisItems.AEK_STATUE);

        simpleItem(GenesisItems.GOD_STATUE_A);
        simpleItem(GenesisItems.GOD_STATUE_B);
        simpleItem(GenesisItems.GOD_STATUE_C);
        simpleItem(GenesisItems.GOD_STATUE_D);
        simpleItem(GenesisItems.GOD_STATUE_E);
        simpleItem(GenesisItems.GOD_STATUE_F);
        simpleItem(GenesisItems.GOD_STATUE_G);
        simpleItem(GenesisItems.GOD_STATUE_H);

        basicItemModel("divine_grail_empty");
        basicItemModel("divine_grail_near_empty");
        basicItemModel("divine_grail_half");
        basicItemModel("divine_grail_most");
        basicItemModel("divine_grail_full");


        ItemModelBuilder grailBuilder = withExistingParent("divine_grail", new ResourceLocation("item/handheld"))
                .texture("layer0", modLoc("item/divine_grail_full")); // 기본 이미지


        grailBuilder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.0f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_empty")))
                .end()
                .override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.1f) // 0.0보다 크면 실행
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_near_empty")))
                .end()
                .override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.4f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_half")))
                .end()
                .override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.7f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_most")))
                .end()
                .override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "fill_level"), 0.9f)
                .model(new ModelFile.UncheckedModelFile(modLoc("item/divine_grail_full")))
                .end();


        // blockitem
        wallItem(GenesisBlocks.FADED_BRICK_WALL, GenesisBlocks.FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.CHISELED_FADED_BRICK);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_BRICK_SLAB);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_STAIRS);
        evenSimpleBlockItem(GenesisBlocks.FADED_STONE_SLAB);
        pillarItem(GenesisBlocks.FADED_PILLAR);

        withExistingParent("mist_vault_1", modLoc("block/mist_vault_1"));
        withExistingParent("hardened_red_glass", modLoc("block/hardened_red_glass"));
        withExistingParent("eye_of_the_earth", modLoc("block/eye_of_the_earth"));

        this.withExistingParent("faded_chest", modLoc("block/faded_chest_closed"));




        //armor
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_HELMET);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_CHESTPLATE);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_LEGGINGS);
        trimmedArmorItem(GenesisItems.PADDED_CHAIN_BOOTS);

        trimmedArmorItem(GenesisItems.SCORPION_HELMET);
        trimmedArmorItem(GenesisItems.SCORPION_CHESTPLATE);
        trimmedArmorItem(GenesisItems.SCORPION_LEGGINGS);
        trimmedArmorItem(GenesisItems.SCORPION_BOOTS);

        simpleItem(GenesisItems.ELVENIA_HELMET);
        simpleItem(GenesisItems.ELVENIA_CHESTPLATE);
        simpleItem(GenesisItems.ELVENIA_LEGGINGS);
        simpleItem(GenesisItems.ELVENIA_BOOTS);

        simpleItem(GenesisItems.ANCIENT_ELVENIA_HELMET);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_CHESTPLATE);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_LEGGINGS);
        simpleItem(GenesisItems.ANCIENT_ELVENIA_BOOTS);

        simpleItem(GenesisItems.CARBONIZED_HELMET);
        simpleItem(GenesisItems.CARBONIZED_CHESTPLATE);
        simpleItem(GenesisItems.CARBONIZED_LEGGINGS);
        simpleItem(GenesisItems.CARBONIZED_BOOTS);

        trimmedArmorItem(GenesisItems.AMETHYST_HELMET);
        trimmedArmorItem(GenesisItems.AMETHYST_CHESTPLATE);
        trimmedArmorItem(GenesisItems.AMETHYST_LEGGINGS);
        trimmedArmorItem(GenesisItems.AMETHYST_BOOTS);

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

        simpleItem(GenesisItems.BAILIFF_HELMET);
        simpleItem(GenesisItems.BAILIFF_CHESTPLATE);
        simpleItem(GenesisItems.BAILIFF_LEGGINGS);
        simpleItem(GenesisItems.BAILIFF_BOOTS);


        simpleItem(GenesisItems.CLOTH_BANDANA);
        simpleItem(GenesisItems.CLOTH_ROBE);
        simpleItem(GenesisItems.CLOTH_LEGGINGS);
        simpleItem(GenesisItems.CLOTH_BOOTS);

        simpleItem(GenesisItems.PILGRIM_BANDANA);
        simpleItem(GenesisItems.PILGRIM_ROBE);
        simpleItem(GenesisItems.PILGRIM_LEGGINGS);
        simpleItem(GenesisItems.PILGRIM_BOOTS);

        simpleItem(GenesisItems.ASTROLOGER_BANDANA);
        simpleItem(GenesisItems.ASTROLOGER_ROBE);
        simpleItem(GenesisItems.ASTROLOGER_LEGGINGS);
        simpleItem(GenesisItems.ASTROLOGER_BOOTS);

        simpleItem(GenesisItems.EMBROIDERED_VEIL);


        simpleItem(GenesisItems.WHITE_IRON_HELMET);
        simpleItem(GenesisItems.WHITE_IRON_CHESTPLATE);
        simpleItem(GenesisItems.WHITE_IRON_LEGGINGS);
        simpleItem(GenesisItems.WHITE_IRON_BOOTS);

        simpleItem(GenesisItems.INTACT_AMETHYST_HEART);
        simpleItem(GenesisItems.STAR_OF_DOMINATION);

        // compass
        String[] types = {"fire", "water", "earth", "storm", "lightning", "plants", "ice"};
        for (String type : types) {
            basicItemModel("spirit_compass_" + type);

        }

    }


    // helper

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



    //override

    private void addCompassOverride(ItemModelBuilder builder, float value, String type) {
        builder.override()
                .predicate(new ResourceLocation(GenesisMod.MODID, "needle_type"), value)
                .model(new ModelFile.UncheckedModelFile("genesis:item/spirit_compass_" + type))
                .end();
    }





    // blockhelper
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
            String itemName = itemRegistryObject.getId().getPath();
            ItemModelBuilder builder = this.withExistingParent(itemName,
                            new ResourceLocation("minecraft", "item/generated"))
                    .texture("layer0", new ResourceLocation(MOD_ID, "item/" + itemName));

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

                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = itemName + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, "item/" + itemName);
                ResourceLocation trimResLoc = new ResourceLocation("minecraft", trimPath);
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, "item/" + currentTrimName);

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);
                builder.override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue)
                        .end();
            });
        }
    }
}
