package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
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
                    List<String> equipped = cap.getEquippedSpells();

                    // 장착된 마법이 최소 1개라도 있는지 확인
                    boolean hasAnySpell = false;
                    for (int i = 0; i < maxSlots; i++) {
                        String s = equipped.get(i);
                        if (s != null && !s.trim().isEmpty()) {
                            hasAnySpell = true;
                            break;
                        }
                    }

                    // 장착된 마법이 있을 때만 슬롯을 변경
                    if (hasAnySpell) {
                        int newSlot = currentSlot;

                        // 최대 maxSlots 만큼만 반복하여 빈칸을 건너뜀 (무한 루프 방지)
                        for (int i = 0; i < maxSlots; i++) {
                            newSlot += msg.direction;

                            // 범위를 벗어나면 반대편으로 루프(순환)
                            if (newSlot < 0) {
                                newSlot = maxSlots - 1;
                            } else if (newSlot >= maxSlots) {
                                newSlot = 0;
                            }

                            // 해당 슬롯에 마법이 있다면 탐색 중지!
                            String spellInSlot = equipped.get(newSlot);
                            if (spellInSlot != null && !spellInSlot.trim().isEmpty()) {
                                break;
                            }
                        }

                        cap.setSelectedSlot(newSlot);

                        // 갱신된 슬롯 정보를 클라이언트로 동기화
                        GenesisNetwork.sendToPlayer(
                                new PacketSyncSpellSlot(cap.getMaxSlots(), cap.getSelectedSlot(), cap.getEquippedSpells()),
                                player
                        );
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}