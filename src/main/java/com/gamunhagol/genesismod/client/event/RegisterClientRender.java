package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RegisterClientRender {

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        BlockPos pos = camera.getBlockPosition();
        BlockState state = camera.getEntity().level().getBlockState(pos);

        if (state.is(GenesisBlocks.SAND_TRAP.get())) {
            event.setRed(0.89F);   // E3DBB0 → RGB 비율로 환산
            event.setGreen(0.86F);
            event.setBlue(0.69F);
        }
    }
}
