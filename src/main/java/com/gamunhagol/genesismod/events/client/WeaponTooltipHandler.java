package com.gamunhagol.genesismod.events.client;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper; // Helper 임포트 추가
import com.gamunhagol.genesismod.world.capability.weapon.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponTooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!WeaponDataManager.hasData(stack.getItem())) return;

        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        long window = Minecraft.getInstance().getWindow().getWindow();
        boolean isTabDown = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_TAB);

        // 강화 수치 표시 (+n)
        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(cap -> {
            int level = cap.getReinforceLevel();
            if (level > 0) {
                Component originalName = event.getToolTip().get(0);
                event.getToolTip().set(0, Component.empty().append(originalName).append(Component.literal(" +" + level).withStyle(ChatFormatting.GOLD)));
            }
        });

        // 상시 표시 로직 (TAB 안 누를 때 기본 속성 대미지 노출)
        if (!isTabDown) {
            addDefaultElementalTooltips(event.getToolTip(), data);
            event.getToolTip().add(Component.translatable("tooltip.genesis.hold_tab").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            // TAB 상세 창 (보너스 수치 및 등급/요구치 노출)
            int currentLevel = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);
            addStatsTooltip(event.getToolTip(), data, event.getEntity(), currentLevel);
        }
    }

    private static void addDefaultElementalTooltips(java.util.List<Component> tooltip, WeaponStatData data) {
        int insertIndex = findAttackDamageIndex(tooltip);
        // 기본 속성 대미지 상시 표시
        if (data.baseMagic() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.magic_damage_simple", String.format("%.1f", data.baseMagic())).withStyle(ChatFormatting.AQUA));
        if (data.baseFire() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.fire_damage_simple", String.format("%.1f", data.baseFire())).withStyle(ChatFormatting.RED));
        if (data.baseLightning() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.lightning_damage_simple", String.format("%.1f", data.baseLightning())).withStyle(ChatFormatting.YELLOW));
        if (data.baseFrost() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.frost_damage_simple", String.format("%.1f", data.baseFrost())).withStyle(ChatFormatting.BLUE));
        if (data.baseHoly() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.holy_damage", String.format("%.1f", data.baseHoly())).withStyle(ChatFormatting.YELLOW));
        if (data.baseDestruction() > 0) tooltip.add(insertIndex, Component.translatable("tooltip.genesis.destruction_damage", String.format("%.1f", data.baseDestruction())).withStyle(ChatFormatting.DARK_RED));
    }

    private static int findAttackDamageIndex(java.util.List<Component> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            String content = tooltip.get(i).getString();
            if (content.contains("공격 피해") || content.contains("Attack Damage")) return i + 1;
        }
        return tooltip.size();
    }

    private static void addStatsTooltip(java.util.List<Component> tooltip, WeaponStatData data, Player player, int level) {
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("tooltip.genesis.stats_header").withStyle(ChatFormatting.GRAY));

        Map<StatType, Float> currentScaling = WeaponRequirementHelper.getCurrentScaling(data, level);

        // [수정됨] 보너스 라인에 cap 대신 player 객체를 넘깁니다.
        addBonusOnlyLine(tooltip, "tooltip.genesis.attack_power", data.basePhysical(), data, level, player, currentScaling, "physical");
        addBonusOnlyLine(tooltip, "tooltip.genesis.magic_damage_label", data.baseMagic(), data, level, player, currentScaling, "magic");
        addBonusOnlyLine(tooltip, "tooltip.genesis.fire_damage_label", data.baseFire(), data, level, player, currentScaling, "fire");
        addBonusOnlyLine(tooltip, "tooltip.genesis.lightning_damage_label", data.baseLightning(), data, level, player, currentScaling, "lightning");
        addBonusOnlyLine(tooltip, "tooltip.genesis.frost_damage_label", data.baseFrost(), data, level, player, currentScaling, "frost");
        addBonusOnlyLine(tooltip, "tooltip.genesis.holy_damage_label", data.baseHoly(), data, level, player, currentScaling, "holy");

        if (data.baseDestruction() > 0) {
            tooltip.add(Component.translatable("tooltip.genesis.destruction_damage_label").append(": " + data.baseDestruction()).withStyle(ChatFormatting.GRAY));
        }

        // 보정 등급 (Scaling) 표시
        MutableComponent scalingLine = Component.translatable("tooltip.genesis.scaling").withStyle(ChatFormatting.GRAY);
        currentScaling.forEach((type, value) -> {
            String grade = getGradeFromValue(value);
            if (!grade.equals("-")) {
                scalingLine.append(Component.literal(" "))
                        .append(Component.translatable("stat.genesis." + type.getName()))
                        .append(Component.literal(" " + grade).withStyle(ChatFormatting.WHITE));
            }
        });
        tooltip.add(scalingLine);

        // 요구 능력치 (Requirements) 표시
        MutableComponent reqLine = Component.translatable("tooltip.genesis.requirements").withStyle(ChatFormatting.GRAY);
        data.requirements().forEach((type, reqValue) -> {
            if (reqValue > 0) {
                // [수정됨] Helper를 사용해 '장비 보너스 포함' 최종 스탯으로 비교합니다.
                int currentPlayerStat = (player != null) ? WeaponRequirementHelper.getPlayerStat(player, type) : 0;
                boolean met = player == null || currentPlayerStat >= reqValue;

                ChatFormatting color = met ? ChatFormatting.WHITE : ChatFormatting.RED;
                reqLine.append(Component.literal(" "))
                        .append(Component.translatable("stat.genesis." + type.getName()))
                        .append(Component.literal(" " + reqValue).withStyle(color));
            }
        });
        tooltip.add(reqLine);
    }

    // [수정됨] cap 대신 player를 받습니다.
    private static void addBonusOnlyLine(java.util.List<Component> tooltip, String langKey, float base, WeaponStatData data, int level, Player player, Map<StatType, Float> scaling, String type) {
        if (base <= 0) return;

        float reinforcedBase = type.equals("physical") ? base * (1.0f + (level * data.damageGrowth())) : base;
        float scalingBonus = calculateScalingOnly(reinforcedBase, player, scaling, type);
        float totalBonus = (reinforcedBase - base) + scalingBonus;

        if (totalBonus > 0) {
            MutableComponent line = Component.translatable(langKey).withStyle(ChatFormatting.GRAY);
            line.append(Component.literal(": ").withStyle(ChatFormatting.WHITE));
            line.append(Component.literal(String.format("+%.1f", totalBonus)).withStyle(ChatFormatting.YELLOW));
            tooltip.add(line);
        }
    }

    // [수정됨] cap 대신 player를 받아서 Helper를 통해 스탯을 조회합니다.
    private static float calculateScalingOnly(float effectiveBase, Player player, Map<StatType, Float> scaling, String type) {
        if (player == null) return 0.0f;
        float bonus = 0.0f;

        for (Map.Entry<StatType, Float> entry : scaling.entrySet()) {
            StatType statType = entry.getKey();
            float scalingGrade = entry.getValue();
            boolean scales = false;

            switch (type) {
                case "physical" -> scales = (statType == StatType.STRENGTH || statType == StatType.DEXTERITY);
                case "magic" -> scales = (statType == StatType.INTELLIGENCE);
                case "holy" -> scales = (statType == StatType.FAITH);
                case "lightning", "frost", "fire" -> scales = (statType == StatType.ARCANE);
            }

            if (statType == StatType.ARCANE) scales = true;

            if (scales) {
                // [수정됨] Helper를 사용해 '장비 보너스 포함' 최종 스탯으로 보정치 계산
                int statValue = WeaponRequirementHelper.getPlayerStat(player, statType);
                bonus += effectiveBase * scalingGrade * com.gamunhagol.genesismod.stats.StatApplier.calculateScaling(statValue);
            }
        }
        return bonus;
    }

    private static String getGradeFromValue(float val) {
        if (val >= 1.5f) return "S";
        if (val >= 1.25f) return "A";
        if (val >= 1.0f) return "B";
        if (val >= 0.75f) return "C";
        if (val >= 0.50f) return "D";
        if (val > 0.0f) return "E";
        return "-";
    }

    // [삭제됨] 이전 getStatValue 메서드는 더 이상 쓰이지 않으므로 제거했습니다.
}