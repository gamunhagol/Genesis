package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class GenesisCapabilityEvents {

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();

        // 1. JSON 데이터팩에 등록된 무기인지 확인 [cite: 2026-02-16]
        if (WeaponDataManager.hasData(item)) {
            // 2. 강화 수치(+N)를 저장할 주머니(Capability)만 깔끔하게 부착 [cite: 2026-02-16]
            // 더 이상 stats.setHolyDamage 같은 초기화 로직은 필요 없습니다. [cite: 2026-02-16]
            WeaponStatsProvider provider = new WeaponStatsProvider();

            event.addCapability(new ResourceLocation(GenesisMod.MODID, "weapon_stats"), provider);
        }
    }
}