package com.gamunhagol.genesismod.network.server;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketCastChargedSpell {
    private final int chargeTicks;

    public PacketCastChargedSpell(int chargeTicks) {
        this.chargeTicks = chargeTicks;
    }

    public PacketCastChargedSpell(FriendlyByteBuf buf) {
        this.chargeTicks = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.chargeTicks);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                int selectedIndex = cap.getSelectedSlot();
                List<String> equipped = cap.getEquippedSpells();
                if (selectedIndex >= 0 && selectedIndex < equipped.size()) {
                    String spellId = equipped.get(selectedIndex);
                    AbstractSpell spell = GenesisSpells.get(spellId);
                    if (spell != null && spell.canCast(player)) {
                        ItemStack mainHand = player.getMainHandItem();
                        DamageSnapshot catalystPower = WeaponRequirementHelper.calculateTotalDamage(player, mainHand, 0f);

                        spell.executeCastCharged(player.level(), player, catalystPower, this.chargeTicks);
                        mainHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                }
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}