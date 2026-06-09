package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketSyncStats {
    private final int vigor, mind, endurance, strength, dexterity, intelligence, faith, arcane;
    private final float mental, maxMental;
    private final boolean isLevelUpUnlocked;
    private final Set<String> learnedSpells;
    private final Set<String> unlockedNodes; // 추가됨

    public PacketSyncStats(int vigor, int mind, int endurance, int strength, int dexterity, int intelligence, int faith, int arcane, float mental, float maxMental, boolean isLevelUpUnlocked, Set<String> learnedSpells, Set<String> unlockedNodes) {
        this.vigor = vigor; this.mind = mind; this.endurance = endurance;
        this.strength = strength; this.dexterity = dexterity; this.intelligence = intelligence;
        this.faith = faith; this.arcane = arcane;
        this.mental = mental; this.maxMental = maxMental;
        this.isLevelUpUnlocked = isLevelUpUnlocked;
        this.learnedSpells = learnedSpells;
        this.unlockedNodes = unlockedNodes;
    }

    public PacketSyncStats(StatCapability cap) {
        this(cap.getVigor(), cap.getMind(), cap.getEndurance(), cap.getStrength(), cap.getDexterity(), cap.getIntelligence(), cap.getFaith(), cap.getArcane(),
                cap.getMental(), cap.getMaxMental(), cap.isLevelUpUnlocked(), cap.getLearnedSpells(), cap.getUnlockedNodes());
    }

    public PacketSyncStats(FriendlyByteBuf buf) {
        this.vigor = buf.readInt(); this.mind = buf.readInt(); this.endurance = buf.readInt();
        this.strength = buf.readInt(); this.dexterity = buf.readInt(); this.intelligence = buf.readInt();
        this.faith = buf.readInt(); this.arcane = buf.readInt();
        this.mental = buf.readFloat(); this.maxMental = buf.readFloat();
        this.isLevelUpUnlocked = buf.readBoolean();
        this.learnedSpells = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
        this.unlockedNodes = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf); // 추가됨
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(vigor); buf.writeInt(mind); buf.writeInt(endurance);
        buf.writeInt(strength); buf.writeInt(dexterity); buf.writeInt(intelligence);
        buf.writeInt(faith); buf.writeInt(arcane);
        buf.writeFloat(mental); buf.writeFloat(maxMental);
        buf.writeBoolean(isLevelUpUnlocked);
        buf.writeCollection(this.learnedSpells, FriendlyByteBuf::writeUtf);
        buf.writeCollection(this.unlockedNodes, FriendlyByteBuf::writeUtf); // 추가됨
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    stats.setVigor(this.vigor); stats.setMind(this.mind); stats.setEndurance(this.endurance);
                    stats.setStrength(this.strength); stats.setDexterity(this.dexterity);
                    stats.setIntelligence(this.intelligence); stats.setFaith(this.faith); stats.setArcane(this.arcane);
                    stats.setMental(this.mental); stats.setMaxMental(this.maxMental);
                    stats.setLevelUpUnlocked(this.isLevelUpUnlocked);

                    stats.getLearnedSpells().clear();
                    stats.getLearnedSpells().addAll(this.learnedSpells);

                    stats.getUnlockedNodes().clear();
                    stats.getUnlockedNodes().addAll(this.unlockedNodes); // 추가됨
                });
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}