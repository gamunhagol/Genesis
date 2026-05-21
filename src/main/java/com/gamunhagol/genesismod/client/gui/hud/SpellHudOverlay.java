package com.gamunhagol.genesismod.client.gui.hud;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import com.gamunhagol.genesismod.world.item.weapon.CatalystItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class SpellHudOverlay {
    public static final IGuiOverlay HUD_SPELL = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        // 플레이어가 없거나, F1(GUI 숨김) 상태이거나, 디버그(F3) 화면이면 그리지 않음
        if (player == null || mc.options.hideGui || mc.options.renderDebug) return;

        // 양손 중 하나라도 '촉매(CatalystItem)'를 들고 있는지 확인
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean holdingCatalyst = mainHand.getItem() instanceof CatalystItem || offHand.getItem() instanceof CatalystItem;

        if (!holdingCatalyst) return; // 촉매를 안 들고 있으면 렌더링 종료

        player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
            int selectedSlotIndex = cap.getSelectedSlot();
            List<String> equipped = cap.getEquippedSpells();
            // HUD 위치 설정 (화면 우측 하단, 핫바 위쪽)
            int iconSize = 24; // 아이콘 크기
            int x = screenWidth - iconSize - 10;
            int y = screenHeight - iconSize - 45; // 핫바(높이 22)를 피해서 위로 올림

            // 장착된 마법 확인
            if (selectedSlotIndex >= 0 && selectedSlotIndex < equipped.size()) {
                String spellId = equipped.get(selectedSlotIndex);

                if (spellId != null && !spellId.isEmpty()) {
                    ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/item/" + spellId + ".png");
                    RenderSystem.enableBlend();
                    // ★ 셰이더 컬러의 알파(4번째 값)를 조절하여 불투명도 설정
                    // 0.6F는 60% 불투명도(약간 흐리게)입니다. 원하는 만큼 수치를 조절해보세요! (0.0F ~ 1.0F)
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.6F);

                    guiGraphics.blit(iconTex, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
                    // ★ 중요: 다음 다른 UI 렌더링에 영향을 주지 않도록 색상과 불투명도를 100%로 리셋합니다.
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                    RenderSystem.disableBlend();
                }
            }
        });
    };
}
