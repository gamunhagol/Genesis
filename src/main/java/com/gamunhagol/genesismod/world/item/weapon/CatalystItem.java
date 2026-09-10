package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.server.PacketCastChargedSpell;
import com.gamunhagol.genesismod.skill.GenesisSkills;
import com.gamunhagol.genesismod.skill.MagicChargeSkill;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.util.GenesisTags;
import com.gamunhagol.genesismod.world.capability.spell.ISpellSlot;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.List;

public class CatalystItem extends Item {

    public CatalystItem(Properties properties) {
        super(properties);
    }

    public boolean canCatalystCastSpell(ItemStack catalyst, AbstractSpell spell) {
        if (spell instanceof MagicSpell) {
            return catalyst.is(GenesisTags.Items.MAGIC_SPELLS);
        } else if (spell instanceof MiracleSpell) {
            return catalyst.is(GenesisTags.Items.MIRACLE_SPELLS);
        }
        return false;
    }

    public AbstractSpell getSelectedSpell(LivingEntity entity) {
        if (entity instanceof Player player) {
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
        return GenesisSpells.LITTLE_HEAL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack catalyst = player.getItemInHand(hand);
        AbstractSpell currentSpell = getSelectedSpell(player);

        if (currentSpell == null || !canCatalystCastSpell(catalyst, currentSpell)) {
            return InteractionResultHolder.fail(catalyst);
        }

        // [핵심] 1. 이미 2타 차징 단계인지 먼저 확인!
        boolean isCharge = currentSpell.isChargePhase(player);

        if (isCharge) {
            player.startUsingItem(hand);

            if (level.isClientSide) {
                LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
                if (playerPatch != null) {
                    SkillContainer skillContainer = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                    if (skillContainer != null) {
                        if (skillContainer.getSkill() == null) {
                            skillContainer.setSkill(GenesisSkills.MAGIC_CHARGE.get(), true);
                        }

                        if (skillContainer.getSkill() instanceof MagicChargeSkill magicCharge) {
                            if (!playerPatch.isHoldingSkill(magicCharge)) {
                                playerPatch.startSkillHolding(magicCharge);
                                magicCharge.startHolding(skillContainer);
                            }
                        }
                    }
                }
            } else {
                EpicFightCapabilities.getPlayerPatchAsOptional(player).ifPresent(patch -> {
                    SkillContainer serverContainer = patch.getSkill(SkillSlots.WEAPON_INNATE);
                    if (serverContainer != null && serverContainer.getSkill() == null) {
                        serverContainer.setSkill(GenesisSkills.MAGIC_CHARGE.get(), true);
                    }
                });
            }
            return InteractionResultHolder.consume(catalyst);
        }

        // 2. 1타 시전일 때만 canCast 사전 검사를 수행
        if (!currentSpell.canCast(player)) {
            return InteractionResultHolder.fail(catalyst);
        }

        // 3. 1타 즉발 시전
        boolean success = castUniversalSpell(level, player, catalyst, currentSpell);
        if (success) {
            return InteractionResultHolder.sidedSuccess(catalyst, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(catalyst);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            AbstractSpell currentSpell = getSelectedSpell(player);
            if (currentSpell != null && currentSpell.isChargePhase(player)) {
                if (level.isClientSide) {
                    LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
                    int chargeTicks = 0;
                    if (playerPatch != null) {
                        chargeTicks = playerPatch.getChargingAmount();
                        playerPatch.resetHolding();
                    }
                    GenesisNetwork.sendToServer(new PacketCastChargedSpell(chargeTicks));
                }
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    public void executeChargedCastOnServer(Player player, ItemStack catalyst, int chargeTicks) {
        AbstractSpell currentSpell = getSelectedSpell(player);
        if (currentSpell != null && currentSpell.canCast(player)) {
            DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(player, catalyst, 0f);
            currentSpell.executeCastCharged(player.level(), player, catalystPower, chargeTicks);
            catalyst.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    public boolean castUniversalSpell(Level level, LivingEntity caster, ItemStack catalyst, AbstractSpell spell) {
        if (spell == null || !canCatalystCastSpell(catalyst, spell)) {
            return false;
        }

        if (spell.canCast(caster)) {
            if (!level.isClientSide) {
                DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(caster, catalyst, 0f);
                spell.executeCast(level, caster, catalystPower);

                if (caster instanceof Player player) {
                    catalyst.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(caster.getUsedItemHand()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, (entity) -> {
            EquipmentSlot slot = (entity.getMainHandItem() == stack) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            entity.broadcastBreakEvent(slot);
        });
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pToRepair.is(GenesisItems.AMETHYST_WAND.get())) return pRepair.is(Items.AMETHYST_SHARD);
        if (pToRepair.is(GenesisItems.GREEN_STAR_SEAL.get())) return pRepair.is(Items.EMERALD);
        if (pToRepair.is(GenesisItems.HAND_HARBORING_OBLIVION.get())) return pRepair.is(GenesisItems.SCATTERED_MEMORIES.get());
        return super.isValidRepairItem(pToRepair, pRepair);
    }
}