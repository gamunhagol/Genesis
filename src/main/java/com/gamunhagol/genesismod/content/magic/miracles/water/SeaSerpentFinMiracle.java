package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;
import java.util.UUID;

public class SeaSerpentFinMiracle extends MiracleSpell {

    public static final UUID SWIM_SPEED_MOD_UUID = UUID.fromString("C5D6E7F8-1234-5678-90AB-CDEF12345678");
    public static final String NBT_KEY_END_TICK = "GenesisSeaSerpentFinEndTick";

    public SeaSerpentFinMiracle() {
        super("sea_serpent_fin");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 6.0f;
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
        if (!level.isClientSide) {
            AttributeInstance swimSpeedAttr = caster.getAttribute(ForgeMod.SWIM_SPEED.get());
            if (swimSpeedAttr != null) {
                swimSpeedAttr.removeModifier(SWIM_SPEED_MOD_UUID);

                swimSpeedAttr.addPermanentModifier(new AttributeModifier(
                        SWIM_SPEED_MOD_UUID,
                        "Genesis Sea Serpent Fin Swim Speed",
                        1.5D,
                        AttributeModifier.Operation.MULTIPLY_BASE
                ));

                int durationTicks = 1200;
                caster.getPersistentData().putLong(NBT_KEY_END_TICK, level.getGameTime() + durationTicks);
            }
        }
    }
}