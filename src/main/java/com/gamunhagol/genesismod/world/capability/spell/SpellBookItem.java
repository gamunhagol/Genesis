package com.gamunhagol.genesismod.world.capability.spell;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SpellBookItem extends Item {
    private final String spellId;

    public SpellBookItem(String spellId, Properties properties) {
        super(properties);
        this.spellId = spellId;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                if (!stats.hasSpell(this.spellId)) {
                    stats.learnSpell(this.spellId);
                    level.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
                            1.0F, 1.0F
                    );
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            });
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        AbstractSpell spell = GenesisSpells.get(this.spellId);
        if (spell == null) return;

        if (Screen.hasAltDown()) {

            Map<StatType, Integer> requiredStats = spell.getRequiredStats();
            if (!requiredStats.isEmpty()) {
                for (Map.Entry<StatType, Integer> entry : requiredStats.entrySet()) {
                    String statNameKey = "stat.genesis." + entry.getKey().getName();
                    int requiredLevel = entry.getValue();

                    tooltip.add(Component.translatable("tooltip.genesis.spell.req_stat",
                            Component.translatable(statNameKey), requiredLevel).withStyle(ChatFormatting.GRAY));
                }
            }

            tooltip.add(Component.translatable("tooltip.genesis.spell.mental_cost", String.format("%.1f", spell.getMentalCost())).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.genesis.spell.memory_cost", spell.getMemoryCost()).withStyle(ChatFormatting.GRAY));

            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("spell.genesis." + this.spellId + ".desc")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));

        } else {
            tooltip.add(Component.translatable("tooltip.genesis.hold_alt")
                    .withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY)));
        }
    }
}