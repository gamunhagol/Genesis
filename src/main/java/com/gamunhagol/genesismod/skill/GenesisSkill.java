package com.gamunhagol.genesismod.skill;

import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class GenesisSkill extends Skill {
    public GenesisSkill(SkillBuilder<? extends GenesisSkill> builder) {
        super(builder);
    }

    protected void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
    }

}
