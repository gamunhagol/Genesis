package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;

public class FrostArmorMiracle extends MiracleSpell {

    public static final String NBT_KEY_END_TICK = "GenesisFrostArmorEndTick";
    public static final UUID FROST_ARMOR_MOD_UUID = UUID.fromString("C1D2E3F4-1111-2222-3333-AABBCCDDEEFF");
    public static final double ARMOR_BONUS = 15.0D;
    public static final long DURATION_TICKS = 2400L;

    public FrostArmorMiracle() {
        super("frost_armor");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 8.9f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        if (caster instanceof Player player) {
            AttributeInstance armorAttr = player.getAttribute(Attributes.ARMOR);
            if (armorAttr != null) {
                armorAttr.removeModifier(FROST_ARMOR_MOD_UUID);
                armorAttr.addPermanentModifier(new AttributeModifier(
                        FROST_ARMOR_MOD_UUID,
                        "Genesis Frost Armor",
                        ARMOR_BONUS,
                        AttributeModifier.Operation.ADDITION
                ));
            }

            player.getPersistentData().putLong(NBT_KEY_END_TICK, level.getGameTime() + DURATION_TICKS);
        }
    }
}
