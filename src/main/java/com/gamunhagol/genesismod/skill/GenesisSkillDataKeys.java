package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.SkillDataKey;

public class GenesisSkillDataKeys {
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS =
            DeferredRegister.create(new ResourceLocation("epicfight", "skill_data_keys"), GenesisMod.MODID);

    public static final RegistryObject<SkillDataKey<Integer>> CUSTOM_COMBO =
            DATA_KEYS.register("custom_combo", () -> SkillDataKey.createSkillDataKey(PacketBufferCodec.INTEGER, 0, GenesisSkill.class));
}
