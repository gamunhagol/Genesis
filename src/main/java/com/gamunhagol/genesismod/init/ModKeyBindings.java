package com.gamunhagol.genesismod.init;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    // 단축키 카테고리 이름 (설정 화면에서 보일 이름)
    public static final String KEY_CATEGORY_GENESIS = "key.categories." + GenesisMod.MODID;

    // 레벨업 화면 단축키 정의
    public static final KeyMapping LEVEL_UP_KEY = new KeyMapping(
            "key." + GenesisMod.MODID + ".level_up", // 번역 키
            KeyConflictContext.IN_GAME,              // 인게임에서만 작동
            InputConstants.Type.KEYSYM,              // 키보드 타입
            GLFW.GLFW_KEY_V,                         // 기본 키: V
            KEY_CATEGORY_GENESIS                     // 카테고리
    );
}
