package com.gamunhagol.genesismod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActivateCustomTotem {
    private final int entityId;
    private final ItemStack totemStack;

    public PacketActivateCustomTotem(int entityId, ItemStack totemStack) {
        this.entityId = entityId;
        this.totemStack = totemStack;
    }

    public PacketActivateCustomTotem(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.totemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeItem(this.totemStack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                Entity entity = mc.level.getEntity(this.entityId);
                if (entity != null) {
                    if (entity == mc.player) {
                        mc.gameRenderer.displayItemActivation(this.totemStack);
                    }

                    mc.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING);
                    mc.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}
