package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.util.LevelCalcHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketConfirmLevelUp {
    private final int[] increases;

    public PacketConfirmLevelUp(int[] increases) { this.increases = increases; }
    public PacketConfirmLevelUp(FriendlyByteBuf buf) { this.increases = buf.readVarIntArray(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeVarIntArray(this.increases); }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || increases == null || increases.length != 8) return;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                int[] currentStats = {stats.getVigor(), stats.getMind(), stats.getEndurance(), stats.getStrength(), stats.getDexterity(), stats.getIntelligence(), stats.getFaith(), stats.getArcane()};

                for (int i = 0; i < 8; i++) {
                    if (increases[i] < 0 || currentStats[i] + increases[i] > 99) {
                        return;
                    }
                }

                int baseLevel = LevelCalcHelper.getCharacterLevel(stats);
                int totalCost = 0;
                int totalInc = 0;
                for (int i : increases) totalInc += i;

                for (int i = 0; i < totalInc; i++) {
                    totalCost += LevelCalcHelper.getXpCostForNextLevel(baseLevel + i);
                }

                int playerTotalXp = LevelCalcHelper.getPlayerTotalXp(player);

                if (playerTotalXp >= totalCost) {
                    player.giveExperiencePoints(-totalCost);
                    stats.setVigor(stats.getVigor() + increases[0]);
                    stats.setMind(stats.getMind() + increases[1]);
                    stats.setEndurance(stats.getEndurance() + increases[2]);
                    stats.setStrength(stats.getStrength() + increases[3]);
                    stats.setDexterity(stats.getDexterity() + increases[4]);
                    stats.setIntelligence(stats.getIntelligence() + increases[5]);
                    stats.setFaith(stats.getFaith() + increases[6]);
                    stats.setArcane(stats.getArcane() + increases[7]);

                    StatApplier.applyAll(player, stats);

                    stats.setDirty(true);
                }
            });
        });
        return true;
    }
}