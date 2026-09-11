package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.ArrowBarrageSpawnerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ArrowRainSpell extends MagicSpell {

    public ArrowRainSpell() {
        super("arrow_rain");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 18);
    }

    @Override
    public float getMentalCost() {
        return 8.0f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalystSnapshot) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 eyePos = caster.getEyePosition();
        Vec3 lookVec = caster.getLookAngle();
        Vec3 endPos = eyePos.add(lookVec.scale(30.0D));

        BlockHitResult hitResult = level.clip(new ClipContext(
                eyePos, endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                caster
        ));

        Vec3 targetGround = (hitResult.getType() != HitResult.Type.MISS) ? hitResult.getLocation() : endPos;

        ArrowBarrageSpawnerEntity spawner = new ArrowBarrageSpawnerEntity(
                GenesisEntities.ARROW_BARRAGE_SPAWNER.get(),
                serverLevel
        );
        spawner.setPos(targetGround.x, targetGround.y + 15.0D, targetGround.z);
        spawner.setup(caster, ArrowBarrageSpawnerEntity.Mode.RAIN, new Vec3(0, -1, 0));

        serverLevel.addFreshEntity(spawner);
        serverLevel.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.2F);
    }
}
