package com.gamunhagol.genesismod.main;

import com.gamunhagol.genesismod.data.loot.GenesisLootTables;
import com.gamunhagol.genesismod.gameasset.GenesisSounds;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.fluid.GenesisFluidTypes;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;
import com.gamunhagol.genesismod.world.item.GenesisCreativeTabs;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;



@Mod(GenesisMod.MODID)
public class GenesisMod {
    public static final String MODID = "genesis";

    public static final Logger LOGGER = LogUtils.getLogger();

    public GenesisMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        GenesisEntities.ENTITY_TYPES.register(modEventBus);
        GenesisItems.ITEMS.register(modEventBus);
        GenesisBlocks.BLOCKS.register(modEventBus);
        GenesisCreativeTabs.TABS.register(modEventBus);
        GenesisSounds.SOUNDS.register(modEventBus);
        GenesisLootTables.LOOT_MODIFIERS.register(modEventBus);


        com.gamunhagol.genesismod.data.recipe.ModRecipeSerializers.SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        GenesisFluidTypes.FLUID_TYPES.register(modEventBus);
        GenesisFluids.FLUIDS.register(modEventBus);





    }


    public static ResourceLocation prefix(String tagName) {
        return null;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

}
