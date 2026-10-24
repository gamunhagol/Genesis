package com.gamunhagol.genesismod.network.client;

import com.gamunhagol.genesismod.stats.StatCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketSyncStats {
    private final int vigor, mind, endurance, strength, dexterity, intelligence, faith, arcane;
    private final float mental, maxMental;
    private final boolean isLevelUpUnlocked;
    private final Set<String> learnedSpells;
    private final Set<String> unlockedNodes;
    private final Set<String> disabledNodes;

    public PacketSyncStats(int vigor, int mind, int endurance, int strength, int dexterity, int intelligence, int faith, int arcane, float mental, float maxMental, boolean isLevelUpUnlocked, Set<String> learnedSpells, Set<String> unlockedNodes, Set<String> disabledNodes) {
        this.vigor = vigor; this.mind = mind; this.endurance = endurance;
        this.strength = strength; this.dexterity = dexterity; this.intelligence = intelligence;
        this.faith = faith; this.arcane = arcane;
        this.mental = mental; this.maxMental = maxMental;
        this.isLevelUpUnlocked = isLevelUpUnlocked;
        this.learnedSpells = learnedSpells;
        this.unlockedNodes = unlockedNodes;
        this.disabledNodes = disabledNodes;
    }

    public PacketSyncStats(StatCapability cap) {
        this(cap.getRawVigor(), cap.getRawMind(), cap.getRawEndurance(), cap.getRawStrength(), cap.getRawDexterity(), cap.getRawIntelligence(), cap.getRawFaith(), cap.getRawArcane(),
                cap.getMental(), cap.getMaxMental(), cap.isLevelUpUnlocked(), cap.getLearnedSpells(), cap.getUnlockedNodes(), cap.getDisabledNodes());
    }

    public PacketSyncStats(FriendlyByteBuf buf) {
        this.vigor = buf.readInt(); this.mind = buf.readInt(); this.endurance = buf.readInt();
        this.strength = buf.readInt(); this.dexterity = buf.readInt(); this.intelligence = buf.readInt();
        this.faith = buf.readInt(); this.arcane = buf.readInt();
        this.mental = buf.readFloat(); this.maxMental = buf.readFloat();
        this.isLevelUpUnlocked = buf.readBoolean();
        this.learnedSpells = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
        this.unlockedNodes = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
        this.disabledNodes = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(vigor); buf.writeInt(mind); buf.writeInt(endurance);
        buf.writeInt(strength); buf.writeInt(dexterity); buf.writeInt(intelligence);
        buf.writeInt(faith); buf.writeInt(arcane);
        buf.writeFloat(mental); buf.writeFloat(maxMental);
        buf.writeBoolean(isLevelUpUnlocked);
        buf.writeCollection(this.learnedSpells, FriendlyByteBuf::writeUtf);
        buf.writeCollection(this.unlockedNodes, FriendlyByteBuf::writeUtf);
        buf.writeCollection(this.disabledNodes, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPayloadHandler.handleStatsSync(
                        this.vigor, this.mind, this.endurance, this.strength, this.dexterity,
                        this.intelligence, this.faith, this.arcane, this.mental, this.maxMental,
                        this.isLevelUpUnlocked, this.learnedSpells, this.unlockedNodes, this.disabledNodes
                );
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
