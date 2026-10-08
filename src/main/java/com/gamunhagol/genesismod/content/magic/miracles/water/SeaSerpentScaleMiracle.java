package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;

public class SeaSerpentScaleMiracle extends MiracleSpell {
    public static final String NBT_KEY_END_TICK = "GenesisSeaSerpentScaleEndTick";
    public static final UUID ARMOR_MOD_UUID = UUID.fromString("B3C4D5E6-F7A8-9012-3456-789ABCDEF011");
    public static final UUID TOUGHNESS_MOD_UUID = UUID.fromString("B3C4D5E6-F7A8-9012-3456-789ABCDEF012");

    public SeaSerpentScaleMiracle() {
        super("sea_serpent_scale");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 20);
    }

    @Override
    public float getMentalCost() {
        return 15.5f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WATER;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        AttributeInstance armorAttr = caster.getAttribute(Attributes.ARMOR);
        if (armorAttr != null) {
            armorAttr.removeModifier(ARMOR_MOD_UUID);
            armorAttr.addPermanentModifier(new AttributeModifier(
                    ARMOR_MOD_UUID,
                    "Sea Serpent Scale Armor",
                    6.0D,
                    AttributeModifier.Operation.ADDITION
            ));
        }

        AttributeInstance toughnessAttr = caster.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughnessAttr != null) {
            toughnessAttr.removeModifier(TOUGHNESS_MOD_UUID);
            toughnessAttr.addPermanentModifier(new AttributeModifier(
                    TOUGHNESS_MOD_UUID,
                    "Sea Serpent Scale Toughness",
                    2.0D,
                    AttributeModifier.Operation.ADDITION
            ));
        }

        caster.getPersistentData().putLong(NBT_KEY_END_TICK, level.getGameTime() + 2100L);

        level.playSound(
                null,
                caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ARMOR_EQUIP_TURTLE,
                caster.getSoundSource(),
                1.0F,
                1.0F
        );
    }
}