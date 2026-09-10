package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class GenesisSkills {
    public static final DeferredRegister<Skill> SKILLS =
            DeferredRegister.create(new ResourceLocation("epicfight", "skill"), GenesisMod.MODID);

    public static final RegistryObject<Skill> MAGIC_CHARGE = SKILLS.register("magic_charge",
            () -> new MagicChargeSkill(WeaponInnateSkill.createWeaponInnateBuilder()
                    .setRegistryName(GenesisMod.prefix("magic_charge"))
                    .setCategory(SkillCategories.WEAPON_INNATE)
                    .setActivateType(Skill.ActivateType.HELD)
                    .setResource(Skill.Resource.NONE)));
}