package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketSyncStats {
    private final int vigor, mind, endurance, strength, dexterity, intelligence, faith, arcane;
    private final float mental, maxMental;
    private final boolean isLevelUpUnlocked; // [추가됨] 해금 여부

    // 생성자 수정
    public PacketSyncStats(int vigor, int mind, int endurance, int strength, int dexterity, int intelligence, int faith, int arcane, float mental, float maxMental, boolean isLevelUpUnlocked) {
        this.vigor = vigor; this.mind = mind; this.endurance = endurance;
        this.strength = strength; this.dexterity = dexterity; this.intelligence = intelligence;
        this.faith = faith; this.arcane = arcane;
        this.mental = mental; this.maxMental = maxMental;
        this.isLevelUpUnlocked = isLevelUpUnlocked; // [추가됨]
    }

    // 디코더 수정 (순서 중요)
    public PacketSyncStats(FriendlyByteBuf buf) {
        this.vigor = buf.readInt(); this.mind = buf.readInt(); this.endurance = buf.readInt();
        this.strength = buf.readInt(); this.dexterity = buf.readInt(); this.intelligence = buf.readInt();
        this.faith = buf.readInt(); this.arcane = buf.readInt();
        this.mental = buf.readFloat(); this.maxMental = buf.readFloat();
        this.isLevelUpUnlocked = buf.readBoolean(); // [추가됨] 받아오기
    }

    // 인코더 수정
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(vigor); buf.writeInt(mind); buf.writeInt(endurance);
        buf.writeInt(strength); buf.writeInt(dexterity); buf.writeInt(intelligence);
        buf.writeInt(faith); buf.writeInt(arcane);
        buf.writeFloat(mental); buf.writeFloat(maxMental);
        buf.writeBoolean(isLevelUpUnlocked); // [추가됨] 보내기
    }

    // 핸들러 수정
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    // 기존 스탯 동기화
                    stats.setVigor(this.vigor); stats.setMind(this.mind); stats.setEndurance(this.endurance);
                    stats.setStrength(this.strength); stats.setDexterity(this.dexterity);
                    stats.setIntelligence(this.intelligence); stats.setFaith(this.faith); stats.setArcane(this.arcane);
                    stats.setMental(this.mental); stats.setMaxMental(this.maxMental);

                    // [추가됨] 해금 여부 동기화
                    stats.setLevelUpUnlocked(this.isLevelUpUnlocked);
                });
            }
        });
        return true;
    }
}