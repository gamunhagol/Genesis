package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeSpell {
    private final int slotIndex;
    private final String spellId;

    public PacketChangeSpell(int slotIndex, String spellId) {
        this.slotIndex = slotIndex;
        this.spellId = spellId;
    }

    public static void encode(PacketChangeSpell msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.slotIndex);
        buf.writeUtf(msg.spellId);
    }

    public static PacketChangeSpell decode(FriendlyByteBuf buf) {
        return new PacketChangeSpell(buf.readInt(), buf.readUtf());
    }

    // 서버 사이드에서 패킷 처리 (Handle)
    public static void handle(PacketChangeSpell msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                    // 1. 서버 데이터 변경
                    cap.setSelectedSlot(msg.slotIndex);
                    cap.equipSpell(msg.spellId);

                    // 2. 변경된 데이터를 다시 클라이언트로 전송하여 화면 동기화 (Dirty 체크 대용)
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncSpellSlot(cap.getMaxSlots(), cap.getSelectedSlot(), cap.getEquippedSpells()),
                            player
                    );
                });
            }
        });
        context.setPacketHandled(true);
    }
}