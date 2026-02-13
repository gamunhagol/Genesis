package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.network.client.ClientPayloadHandler;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncMentalPower {
    private final float mentalPower;

    // 1. 생성자: 데이터를 담을 때 씀
    public PacketSyncMentalPower(float mentalPower) {
        this.mentalPower = mentalPower;
    }

    // 2. 디코더: 받은 패킷(바이트)을 다시 데이터로 변환
    public PacketSyncMentalPower(FriendlyByteBuf buf) {
        this.mentalPower = buf.readFloat();
    }

    // 3. 인코더: 데이터를 통신용 바이트로 변환
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(this.mentalPower);
    }

    // 4. 핸들러: 택배가 도착했을 때 행동 (클라이언트 쪽 로직)
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        // 클라이언트 사이드에서만 안전하게 실행되도록 보장
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPayloadHandler.handleMentalSync(this.mentalPower);
            });
        });
        return true;
    }
}