package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.spell.ISpellSlot;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import com.gamunhagol.genesismod.world.item.GenesisItems;
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

    // 플레이어의 Spell Capability를 조회하여 현재 활성화된 슬롯의 주문을 동적으로 가져옵니다.
    protected AbstractSpell getSelectedSpell(LivingEntity entity) {
        if (entity instanceof Player player) {

            // 핵심 수정: map() 안에서 null을 반환하면 크래시가 나므로,
            // orElse(null)을 통해 Capability(ISpellSlot) 객체를 먼저 안전하게 꺼냅니다.
            ISpellSlot cap = player.getCapability(SpellSlotProvider.SPELL_SLOT).orElse(null);

            if (cap != null) {
                int selectedIndex = cap.getSelectedSlot();
                List<String> equipped = cap.getEquippedSpells();

                // [안전장치 1] 리스트 자체가 null이 아닌지, 인덱스가 정상 범위 내에 있는지 확실하게 체크
                if (equipped != null && selectedIndex >= 0 && selectedIndex < equipped.size()) {
                    String spellId = equipped.get(selectedIndex);

                    // [안전장치 2] 문자열이 null이 아니고, 공백 제거 후에도 비어있지 않은지 체크
                    if (spellId != null && !spellId.trim().isEmpty()) {

                        // [안전장치 3] GenesisSpells에서 등록된 마법 객체를 가져와 반환합니다.
                        return GenesisSpells.get(spellId);
                    }
                }
            }

            // Capability가 없거나, 슬롯이 비었거나, 조건에 맞지 않으면 최종적으로 null 반환
            return null;
        }

        // 플레이어가 아닌 몹이 사용할 때를 위한 예외 처리용 기본 마법
        return GenesisSpells.HEAL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack catalyst = player.getItemInHand(hand);
        AbstractSpell currentSpell = getSelectedSpell(player);

        // [핵심 예외 처리] 슬롯이 빈 칸이거나 장착된 마법이 없다면 아무 일도 하지 않고 Fail 처리
        if (currentSpell == null) {
            // pass를 쓰면 다른 블록이나 오프핸드 아이템이 작동해버릴 수 있으므로, 확실하게 행동을 차단(fail)합니다.
            return InteractionResultHolder.fail(catalyst);
        }

        boolean success = castUniversalSpell(level, player, catalyst, currentSpell);

        if (success) {
            return InteractionResultHolder.sidedSuccess(catalyst, level.isClientSide());
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.fail(catalyst);
        }
    }

    // 몹과 플레이어가 모두 사용하는 범용 시전 로직
    public boolean castUniversalSpell(Level level, LivingEntity caster, ItemStack catalyst, AbstractSpell spell) {
        // [안전장치 4] 혹시라도 null인 마법이 여기까지 넘어왔을 때 크래시를 막기 위한 최종 방어막
        if (spell == null) {
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
        return super.isValidRepairItem(pToRepair, pRepair);
    }
}