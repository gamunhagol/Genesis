package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedVexEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class SummonVexSpell extends AbstractSummonSpell {

    public SummonVexSpell() {
        super("summon_vex");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 14);
    }

    @Override
    public float getMentalCost() {
        return 9.0f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!super.canCast(caster)) {
            return false;
        }

        if (caster instanceof Player player) {
            List<SummonedVexEntity> existingVexes = player.level().getEntitiesOfClass(
                    SummonedVexEntity.class,
                    player.getBoundingBox().inflate(128.0D),
                    vex -> player.getUUID().equals(vex.getOwnerUUID())
            );
            return existingVexes.size() < 10;
        }

        return true;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        super.onExecute(level, caster, spellSnapshot);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel && caster instanceof Player player) {
            List<SummonedVexEntity> currentVexes = serverLevel.getEntitiesOfClass(
                    SummonedVexEntity.class,
                    player.getBoundingBox().inflate(128.0D),
                    vex -> player.getUUID().equals(vex.getOwnerUUID())
            );

            if (currentVexes.size() < 10) {
                SummonedVexEntity extraVex = createVex(serverLevel, player);
                extraVex.moveTo(player.getX(), player.getY() + 0.5D, player.getZ(), player.getYRot(), player.getXRot());
                serverLevel.addFreshEntity(extraVex);
            }
        }
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        return createVex(level, caster);
    }

    private SummonedVexEntity createVex(ServerLevel level, Player caster) {
        SummonedVexEntity vex = new SummonedVexEntity(GenesisEntities.SUMMONED_VEX.get(), level);
        vex.setOwnerUUID(caster.getUUID());

        vex.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        vex.setDropChance(EquipmentSlot.MAINHAND, 0.0f);

        return vex;
    }
}