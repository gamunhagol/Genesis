package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.skill.weaponinnate.GreatBowSpecialShot;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillCategories;

public class GenesisSkills {
    public static final DeferredRegister<Skill> SKILLS =
            DeferredRegister.create(new ResourceLocation("epicfight", "skills"), GenesisMod.MODID);

    public static final RegistryObject<Skill> GREAT_BOW_SPECIAL_SHOT = SKILLS.register("great_bow_special_shot",
            () -> new GreatBowSpecialShot(
                    new SkillBuilder<GreatBowSpecialShot>() // 여기서 제네릭 타입을 명시!
                            .setCategory(SkillCategories.WEAPON_INNATE)
                            .setResource(Skill.Resource.STAMINA)
            ));
}