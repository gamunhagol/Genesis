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

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean holdingCatalyst = mainHand.getItem() instanceof CatalystItem || offHand.getItem() instanceof CatalystItem;

        if (!holdingCatalyst) return;

        player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
            int selectedSlotIndex = cap.getSelectedSlot();
            List<String> equipped = cap.getEquippedSpells();
            // HUD 위치 설정
            int iconSize = 24;
            int x = screenWidth - iconSize - 10;
            int y = screenHeight - iconSize - 45;

            // 장착된 마법 확인
            if (selectedSlotIndex >= 0 && selectedSlotIndex < equipped.size()) {
                String spellId = equipped.get(selectedSlotIndex);

                if (spellId != null && !spellId.isEmpty()) {
                    ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/item/" + spellId + ".png");
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.6F);

                    guiGraphics.blit(iconTex, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                    RenderSystem.disableBlend();
                }
            }
        });
    };
}
