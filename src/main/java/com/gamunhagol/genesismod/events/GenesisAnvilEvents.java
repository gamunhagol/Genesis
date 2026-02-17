package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.content.ReinforceManager;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisAnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();   // 왼쪽: 무기
        ItemStack right = event.getRight(); // 오른쪽: 재료

        // 1. 우리 모드에 데이터가 등록된 무기인지 확인
        if (left.isEmpty() || !WeaponDataManager.hasData(left.getItem())) return;

        // 2. 현재 강화 수치 확인
        // (주의: Capability는 아이템 스택의 NBT가 아니라 런타임 객체라, 여기서는 copy 전 원본에서 데이터를 읽어야 함)
        // 하지만 AnvilUpdateEvent에서는 Capability에 직접 접근하기 까다로울 수 있어,
        // 보통 getCapability를 쓰되, 안 되면 NBT를 직접 읽는 방어 코드를 씁니다.
        int currentLevel = 0;
        var cap = left.getCapability(WeaponStatsProvider.WEAPON_STATS).orElse(null);
        if (cap != null) {
            currentLevel = cap.getReinforceLevel();
        }

        // 3. 다음 레벨에 필요한 재료 확인
        WeaponStatData data = WeaponDataManager.get(left.getItem());
        int nextLevel = currentLevel + 1;
        int maxLevel = ReinforceManager.getMaxLevel(data.isSpecial());

        // 이미 풀강이면 패스
        if (nextLevel > maxLevel) return;

        Item requiredMaterial = data.isSpecial()
                ? ReinforceManager.getSpecialMaterial(nextLevel)
                : ReinforceManager.getStandardMaterial(nextLevel);

        // 4. 재료가 맞는지 확인 (개수 1개 이상)
        if (requiredMaterial != null && right.getItem() == requiredMaterial && right.getCount() >= 1) {

            // 5. 결과물 생성
            ItemStack output = left.copy();

            // 6. 결과물의 강화 수치 증가 (+1)
            // Capability 데이터를 NBT로 직접 조작해야 안전하게 출력물에 저장됨
            // (CapabilityProvider가 serializeNBT를 통해 "ReinforceLevel" 키를 쓴다고 가정)
            // 주의: Forge Capability는 ItemStack이 복사될 때 NBT가 같이 복사되지만,
            // 값을 수정하려면 Capability를 다시 열어서 수정해야 함.
            final int finalNextLevel = nextLevel;
            output.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
                stats.setReinforceLevel(finalNextLevel);
            });

            // 7. 모루 UI 설정
            event.setOutput(output);
            event.setCost(nextLevel); // 경험치 비용: 다음 레벨만큼 (예: 1강 가는데 1레벨, 25강 가는데 25레벨)
            event.setMaterialCost(1); // 재료 1개 소모
        }
    }
}
