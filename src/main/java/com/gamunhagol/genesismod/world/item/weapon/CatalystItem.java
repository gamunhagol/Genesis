package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.util.GenesisTags;
import com.gamunhagol.genesismod.world.capability.spell.ISpellSlot;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

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

    protected AbstractSpell getSelectedSpell(LivingEntity entity) {
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

        // 플레이어가 아닌 몹이 사용할 때를 위한 예외 처리용 기본 마법
        return GenesisSpells.HEAL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack catalyst = player.getItemInHand(hand);
        AbstractSpell currentSpell = getSelectedSpell(player);

        if (currentSpell == null) {
            return InteractionResultHolder.fail(catalyst);
        }
        if (!canCatalystCastSpell(catalyst, currentSpell)) {
            return InteractionResultHolder.fail(catalyst);
        }

        boolean success = castUniversalSpell(level, player, catalyst, currentSpell);

        if (success) {
            return InteractionResultHolder.sidedSuccess(catalyst, level.isClientSide());
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);//모션 추가되기 전까지 임시 사용
            return InteractionResultHolder.fail(catalyst);
        }
    }

    // 몹과 플레이어가 모두 사용하는 범용 시전 로직
    public boolean castUniversalSpell(Level level, LivingEntity caster, ItemStack catalyst, AbstractSpell spell) {
        if (spell == null || !canCatalystCastSpell(catalyst, spell)) {
            return false;
        }

        if (spell.canCast(caster)) {
            if (!level.isClientSide) {
                DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(caster, catalyst, 0f);
                spell.executeCast(level, caster, catalystPower);

                // 마법 시전 시 내구도 1 소모 (플레이어만)
                if (caster instanceof Player player) {
                    catalyst.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(caster.getUsedItemHand()));
                }
            }
            return true;
        }
        return false;
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
        if (enchantment == Enchantments.UNBREAKING) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pToRepair.is(GenesisItems.AMETHYST_WAND.get())) {
            return pRepair.is(Items.AMETHYST_SHARD);
        }
        if (pToRepair.is(GenesisItems.GREEN_STAR_SEAL.get())) {
            return pRepair.is(Items.EMERALD);
        }
        if (pToRepair.is(GenesisItems.HAND_HARBORING_OBLIVION.get())) {
            return pRepair.is(GenesisItems.SCATTERED_MEMORIES.get());
        }
        return super.isValidRepairItem(pToRepair, pRepair);
    }
}