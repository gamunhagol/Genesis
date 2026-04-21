package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import yesman.epicfight.skill.Skill;

public class GenesisSkills {
    public static final DeferredRegister<Skill> SKILLS =
            DeferredRegister.create(new ResourceLocation("epicfight", "skills"), GenesisMod.MODID);


}