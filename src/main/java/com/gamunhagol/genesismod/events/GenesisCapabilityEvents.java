package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.capability.WeaponStatsRegistry;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class GenesisCapabilityEvents {

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();

        // 에픽파이트 무기 등 검 종류인 경우
        if (item instanceof SwordItem) {
            // 우리가 만든 레지스트리에서 이 아이템의 데이터를 가져옵니다.
            WeaponStatsRegistry.WeaponData data = WeaponStatsRegistry.getStats(item);

            // 기본값이 0이 아니라면 (등록된 무기라면) Capability 부착
            if (data.holy() > 0 || data.destruction() > 0) {
                WeaponStatsProvider provider = new WeaponStatsProvider();

                provider.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
                    stats.setHolyDamage(data.holy());
                    stats.setDestructionDamage(data.destruction());
                });

                event.addCapability(new ResourceLocation(GenesisMod.MODID, "weapon_stats"), provider);
            }
        }
    }
}
