package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class ThrowBombSpell extends MagicSpell {
    public ThrowBombSpell() { super("throw_bomb"); }

    @Override
    public int getCastTime() { return 10; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 10);
    }

    @Override
    public float getMentalCost() { return 4.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        CompoundTag data = caster.getPersistentData();

        if (data.hasUUID("GenesisGrabbedEntity")) {
            UUID uuid = data.getUUID("GenesisGrabbedEntity");
            Entity grabbed = serverLevel.getEntity(uuid);
            data.remove("GenesisGrabbedEntity");

            if (grabbed != null) {
                Vec3 pos = grabbed.position();
                grabbed.discard();

                Vec3 look = caster.getLookAngle();

                PrimedTnt tnt = new PrimedTnt(level, pos.x, pos.y, pos.z, null);
                tnt.setFuse(40);
                tnt.setDeltaMovement(look.scale(1.5D));

                level.addFreshEntity(tnt);
                level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        else {
            Vec3 look = caster.getLookAngle();
            Vec3 spawnPos = caster.getEyePosition().add(look.scale(2.0D));

            FallingBlockEntity fakeTnt = FallingBlockEntity.fall(level, BlockPos.containing(spawnPos), Blocks.TNT.defaultBlockState());

            fakeTnt.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            fakeTnt.setNoGravity(true);
            fakeTnt.time = 1;

            data.putUUID("GenesisGrabbedEntity", fakeTnt.getUUID());

            level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.CREEPER_PRIMED, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}