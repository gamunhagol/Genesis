package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTooltipHandler {

    // 아이템과 툴팁 키를 연결해줄 맵(Map)
    private static final Map<Item, String> LORE_MAP = new HashMap<>();

    /**
     * 모드 초기화 단계(FMLClientSetupEvent)에서 호출하여
     * 어떤 아이템에 어떤 툴팁을 띄울지 등록하는 메서드입니다.
     */
    public static void init() {
        // 여기에 툴팁을 추가하고 싶은 아이템들을 등록하세요.
        register(GenesisItems.BOOK_OF_CREATION.get(), "tooltip.genesis.book_of_creation.info");

        register(GenesisItems.DREAM_POWDER.get(), "tooltip.genesis.dream_powder.info");
        register(GenesisItems.DREAM_DANGO.get(), "tooltip.genesis.dream_dango.info");
        register(GenesisItems.REMNANTS_OF_A_DREAM.get(), "tooltip.genesis.remnants_of_a_dream.info");
        register(GenesisItems.FRAGMENT_OF_MEMORY.get(), "tooltip.genesis.fragment_of_memory.info");

        register(GenesisItems.FLASK_SHARD.get(), "tooltip.genesis.flask_shard.info");
        register(GenesisItems.BEAST_REMAINS.get(), "tooltip.genesis.beast_remains.info");
        register(GenesisItems.DIVINE_GRAIL.get(), "tooltip.genesis.divine_grail.info");

        register(GenesisItems.OPAQUE_JELLY.get(), "tooltip.genesis.opaque_jelly.info");

        register(GenesisItems.SCALE_FOSSIL_SHARD.get(), "tooltip.genesis.scale_fossil_shard.info");
        register(GenesisItems.SCALE_FOSSIL.get(), "tooltip.genesis.scale_fossil.info");
        register(GenesisItems.SCALE_FOSSIL_CLUMP.get(), "tooltip.genesis.scale_fossil_clump.info");
        register(GenesisItems.WEATHERED_ANCIENT_DRAGON_ROCK.get(), "tooltip.genesis.weathered_ancient_dragon_rock.info");
        register(GenesisItems.ANCIENT_DRAGON_ROCK.get(), "tooltip.genesis.ancient_dragon_rock.info");
        register(GenesisItems.ANCIENT_DRAGON_SCALE.get(), "tooltip.genesis.ancient_dragon_scale.info");
        register(GenesisItems.DRAGON_KING_SCALE.get(), "tooltip.genesis.dragon_king_scale.info");

        register(GenesisItems.SHARD_OF_THE_MOUNTAIN.get(), "tooltip.genesis.shard_of_the_mountain.info");
        register(GenesisItems.FRAGMENT_OF_THE_MOUNTAIN.get(), "tooltip.genesis.fragment_of_the_mountain.info");
        register(GenesisItems.CLUMP_OF_THE_MOUNTAIN.get(), "tooltip.genesis.clump_of_the_mountain.info");
        register(GenesisItems.TABLET_SHARD.get(), "tooltip.genesis.tablet_shard.info");
        register(GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN.get(), "tooltip.genesis.tablet_of_the_radiant_mountain.info");


        register(GenesisItems.KEY_OF_OBLIVION.get(), "tooltip.genesis.key_of_oblivion.info");

        register(GenesisItems.MISY_CORE_1.get(), "tooltip.genesis.mist_core_1.info");


        register(GenesisItems.MEDALLION_OF_DOMINION.get(), "tooltip.genesis.medallion_of_dominion.info");

        register(GenesisItems.FABRICATED_STAR.get(), "tooltip.genesis.fabricated_star.info");


        // 나중에 아이템이 늘어나면 여기에 한 줄씩만 추가하면 됩니다.
    }

    private static void register(Item item, String key) {
        LORE_MAP.put(item, key);
    }

    private static void add(RegistryObject<? extends Item> item, String key) {
        LORE_MAP.put(item.get(), key);
    }
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();


        if (LORE_MAP.containsKey(item)) {
            String translationKey = LORE_MAP.get(item);
            applyLore(event.getToolTip(), translationKey);
        }
    }

    private static void applyLore(List<Component> tooltip, String translationKey) {
        if (Screen.hasAltDown()) {
            tooltip.add(Component.translatable(translationKey)
                    .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.genesis.hold_alt") // 키 이름도 변경
                    .withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY)));
        }
    }
}
