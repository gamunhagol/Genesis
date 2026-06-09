package com.gamunhagol.genesismod.init;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String KEY_CATEGORY_GENESIS = "key.categories." + GenesisMod.MODID;

    public static final KeyMapping LEVEL_UP_KEY = new KeyMapping(
            "key." + GenesisMod.MODID + ".level_up",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KEY_CATEGORY_GENESIS
    );

    public static final KeyMapping SPELL_PREV_KEY = new KeyMapping(
            "key." + GenesisMod.MODID + ".spell_prev",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            KEY_CATEGORY_GENESIS
    );
    public static final KeyMapping SPELL_NEXT_KEY = new KeyMapping(
            "key." + GenesisMod.MODID + ".spell_next",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_DOWN,
            KEY_CATEGORY_GENESIS
    );
}
