package com.gamunhagol.genesismod.skill;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.init.ModKeyBindings;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class MagicChargeSkill extends AbstractCatalystSpellSkill {

    public MagicChargeSkill(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder);
    }

    @Override
    public int getAllowedMaxChargingTicks() {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            Player player = ClientEngine.getInstance().getPlayerPatch() != null
                    ? (Player) ClientEngine.getInstance().getPlayerPatch().getOriginal() : null;
            AbstractSpell spell = getPlayerSelectedSpell(player);
            return spell != null ? spell.getMaxChargeTicks() : 120;
        });
    }

    @Override
    public int getMaxChargingTicks() {
        return getAllowedMaxChargingTicks();
    }

    @Override
    public int getMinChargingTicks() {
        return 0;
    }

    @Override
    public void startHolding(SkillContainer container) {
        super.startHolding(container);
        if (container.getClientExecutor() != null) {
        }
    }

    @Override
    public KeyMapping getKeyMapping() {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ModKeyBindings.SPELL_CAST_KEY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDraw(SkillContainer container) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y, float partialTick) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getSkillTexture() {
        if (this.getRegistryName() != null) {
            return super.getSkillTexture();
        }
        return EpicFightSkills.STEEL_WHIRLWIND.getSkillTexture();
    }

    @Override
    protected void onExecuteSpellOnServer(SkillContainer container, Player player, AbstractSpell spell, int chargeTicks) {
        if (spell == null || !spell.canCast(player)) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(player, mainHand, 0f);

        spell.executeCastCharged(player.level(), player, catalystPower, chargeTicks);
        mainHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
    }
}