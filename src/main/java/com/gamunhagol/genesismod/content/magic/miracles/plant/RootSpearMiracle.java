package com.gamunhagol.genesismod.content.magic.miracles.plant;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.etc.child.RootSpearEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class RootSpearMiracle extends MiracleSpell {

    public RootSpearMiracle() {
        super("root_spear");
    }

    @Override
    public int getMaxChargeTicks() {
        return 100;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return true;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 20,
                StatType.ARCANE, 12
        );
    }

    @Override
    public float getMentalCost() {
        return 9.2F;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FOREST;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 10.0F;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.7F)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        spawnRoots(level, caster, spellSnapshot, 3.0D, 6, 9);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0F, (float) chargeTicks / (float) getMaxChargeTicks());

        if (ratio >= 0.75F) {
            spawnRoots(level, caster, spellSnapshot, 8.0D, 24, 30);
        } else if (ratio >= 0.35F) {
            spawnRoots(level, caster, spellSnapshot, 6.0D, 15, 21);
        } else {
            spawnRoots(level, caster, spellSnapshot, 3.0D, 6, 9);
        }
    }

    private void spawnRoots(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, double areaSize, int minCount, int maxCount) {
        Vec3 centerPos = caster.position();

        int count = minCount + level.random.nextInt(maxCount - minCount + 1);
        for (int i = 0; i < count; i++) {
            double rx = (level.random.nextDouble() - 0.5D) * areaSize;
            double rz = (level.random.nextDouble() - 0.5D) * areaSize;

            BlockPos targetPos = findFloorPos(level, BlockPos.containing(centerPos.x + rx, centerPos.y, centerPos.z + rz));
            if (targetPos != null) {
                RootSpearEntity spear = new RootSpearEntity(level, caster, spellSnapshot);
                spear.setPos(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D);
                level.addFreshEntity(spear);
            }
        }
    }

    private BlockPos findFloorPos(Level level, BlockPos startPos) {
        BlockPos.MutableBlockPos cursor = startPos.mutable();
        for (int dy = 5; dy >= -5; dy--) {
            cursor.set(startPos.getX(), startPos.getY() + dy, startPos.getZ());
            BlockState state = level.getBlockState(cursor);

            if (state.isFaceSturdy(level, cursor, Direction.UP)) {
                BlockPos above = cursor.above();
                if (level.getBlockState(above).canBeReplaced()) {
                    return above;
                }
            }
        }
        return null;
    }
}
