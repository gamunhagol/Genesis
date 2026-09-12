package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.client.PacketSyncSpatialRupture;
import com.gamunhagol.genesismod.world.border.SpatialRuptureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SpatialRuptureSpell extends MagicSpell {

    public SpatialRuptureSpell() {
        super("spatial_rupture");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 30);
    }

    @Override
    public float getMentalCost() {
        return 30.0f;
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
        if (level.isClientSide) return;

        if (!(level instanceof ServerLevel serverLevel)) return;
        MinecraftServer server = serverLevel.getServer();

        long expireTime = server.overworld().getGameTime() + 1800L;

        ResourceLocation currentDim = serverLevel.dimension().location();
        double centerX = caster.getX();
        double centerZ = caster.getZ();
        double radius = 3.0D;

        SpatialRuptureManager.addZone(currentDim, centerX, centerZ, radius, expireTime);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.level().dimension().location().equals(currentDim)) {
                GenesisNetwork.sendToPlayer(
                        new PacketSyncSpatialRupture(currentDim, centerX, centerZ, radius, expireTime),
                        player
                );
            }
        }
    }
}