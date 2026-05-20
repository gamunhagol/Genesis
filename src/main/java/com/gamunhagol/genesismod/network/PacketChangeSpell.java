package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
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

                    // 해제 요청인 경우 (빈 문자열) - 검사 없이 바로 해제
                    if (msg.spellId == null || msg.spellId.isEmpty()) {
                        cap.setSelectedSlot(msg.slotIndex);
                        cap.equipSpell("");
                    }
                    // 장착 요청인 경우 - 코스트(메모리) 계산
                    else {
                        AbstractSpell targetSpell = GenesisSpells.get(msg.spellId);
                        if (targetSpell != null) {
                            int newSpellCost = targetSpell.getMemoryCost();
                            int currentTotalCost = 0;

                            // 현재 장착된 모든 마법의 코스트 합산 (바꾸려는 슬롯의 마법은 제외)
                            List<String> equipped = cap.getEquippedSpells();
                            for (int i = 0; i < equipped.size(); i++) {
                                if (i == msg.slotIndex) continue; // 덮어씌울 칸의 기존 코스트는 제외

                                String spellId = equipped.get(i);
                                if (spellId != null && !spellId.isEmpty()) {
                                    AbstractSpell s = GenesisSpells.get(spellId);
                                    if (s != null) {
                                        currentTotalCost += s.getMemoryCost();
                                    }
                                }
                            }

                            // ★ 코스트 한도 초과 검사
                            if (currentTotalCost + newSpellCost <= cap.getMemoryCapacity()) {
                                // 장착 성공
                                cap.setSelectedSlot(msg.slotIndex);
                                cap.equipSpell(msg.spellId);
                            } else {
                                // 장착 실패 (슬롯 부족) - 실패 사운드 재생
                                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                        SoundEvents.FIRE_EXTINGUISH,
                                        SoundSource.PLAYERS, 1.0F, 1.0F);
                            }
                        }
                    }

                    // 3. 결과 동기화 (성공이든 실패든 현재 상태를 클라로 다시 보내 UI 리프레시)
                    GenesisNetwork.sendToPlayer(
                            new PacketSyncSpellSlot(cap.getMemoryCapacity(), cap.getSelectedSlot(), cap.getEquippedSpells()),
                            player
                    );
                });
            }
        });
        context.setPacketHandled(true);
    }
}