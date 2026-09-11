package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.world.capability.spell.ISpellSlot;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.client.CPSkillRequest;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.modules.ChargeableSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

public abstract class AbstractCatalystSpellSkill extends WeaponInnateSkill implements ChargeableSkill {

    public AbstractCatalystSpellSkill(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder);
    }

    protected AbstractSpell getPlayerSelectedSpell(Player player) {
        if (player == null) {
            return null;
        }
        ISpellSlot cap = player.getCapability(SpellSlotProvider.SPELL_SLOT).orElse(null);
        if (cap != null) {
            int selectedIndex = cap.getSelectedSlot();
            List<String> equipped = cap.getEquippedSpells();
            if (equipped != null && selectedIndex >= 0 && selectedIndex < equipped.size()) {
                String spellId = equipped.get(selectedIndex);
                if (spellId != null && !spellId.trim().isEmpty()) {
                    return GenesisSpells.get(spellId);
                }
            }
        }
        return null;
    }


    @Override
    public String getTranslationKey() {
        if (net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT) {
            LocalPlayerPatch patch = ClientEngine.getInstance().getPlayerPatch();
            if (patch != null && patch.getOriginal() != null) {
                AbstractSpell spell = getPlayerSelectedSpell((Player) patch.getOriginal());
                if (spell != null) {
                    return spell.getDescriptionId();
                }
            }
        }
        return super.getTranslationKey();
    }

    @Override
    public Component getDisplayName() {
        if (net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT) {
            LocalPlayerPatch patch = ClientEngine.getInstance().getPlayerPatch();
            if (patch != null && patch.getOriginal() != null) {
                AbstractSpell spell = getPlayerSelectedSpell((Player) patch.getOriginal());
                if (spell != null) {
                    return spell.getName();
                }
            }
        }
        return super.getDisplayName();
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        if (!super.canExecute(container)) {
            return false;
        }
        Player player = (Player) container.getExecutor().getOriginal();
        AbstractSpell spell = getPlayerSelectedSpell(player);
        return spell != null && spell.canCast(player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object getExecutionPacket(SkillContainer container, FriendlyByteBuf args) {
        CPSkillRequest packet = new CPSkillRequest(container.getSlot(), CPSkillRequest.WorkType.CAST);
        LocalPlayerPatch patch = container.getClientExecutor();
        int chargeTicks = patch != null ? patch.getChargingAmount() : 0;
        packet.getBuffer().writeInt(chargeTicks);
        return packet;
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        Player player = (Player) container.getExecutor().getOriginal();
        AbstractSpell spell = getPlayerSelectedSpell(player);

        int chargeTicks = 0;
        if (args != null && args.isReadable()) {
            chargeTicks = args.readInt();
        } else if (container.getServerExecutor() != null) {
            chargeTicks = container.getServerExecutor().getAccumulatedChargeAmount();
        }

        try {
            onExecuteSpellOnServer(container, player, spell, chargeTicks);
        } finally {
            if (container.getServerExecutor() != null) {
                container.getServerExecutor().resetHolding();
            }
            EpicFightCapabilities.getPlayerPatchAsOptional(player).ifPresent(PlayerPatch::resetHolding);
        }
    }

    @Override
    public void startHolding(SkillContainer container) {
        ChargeableSkill.super.startHolding(container);
    }

    protected abstract void onExecuteSpellOnServer(SkillContainer container, Player player, AbstractSpell spell, int chargeTicks);
}