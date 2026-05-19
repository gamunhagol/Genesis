package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeSelectedSlot {
    private final int direction; // -1: 이전 슬롯, 1: 다음 슬롯

    public PacketChangeSelectedSlot(int direction) {
        this.direction = direction;
    }

    public static void encode(PacketChangeSelectedSlot msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.direction);
    }

    public static PacketChangeSelectedSlot decode(FriendlyByteBuf buf) {
        return new PacketChangeSelectedSlot(buf.readInt());
    }

    public static void handle(PacketChangeSelectedSlot msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                    int currentSlot = cap.getSelectedSlot();
                    int maxSlots = cap.getMaxSlots(); // 해금된 최대 슬롯 수

                    // 새 슬롯 계산
                    int newSlot = currentSlot + msg.direction;

                    // 범위를 벗어나면 반대편으로 루프(순환)되도록 처리
                    if (newSlot < 0) {
                        newSlot = maxSlots - 1; // 0에서 위로 가면 맨 끝 슬롯으로
                    } else if (newSlot >= maxSlots) {
                        newSlot = 0; // 끝에서 아래로 가면 0번 슬롯으로
                    }

                    cap.setSelectedSlot(newSlot);

                    // 갱신된 슬롯 정보를 클라이언트로 동기화 (화면에 반영)
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