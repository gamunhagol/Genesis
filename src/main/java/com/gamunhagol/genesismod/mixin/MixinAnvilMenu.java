package com.gamunhagol.genesismod.mixin;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {

    @Shadow
    @Final
    private DataSlot cost;

    public MixinAnvilMenu(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }


    @Inject(method = "createResult", at = @At("TAIL"))
    private void genesis$applyDiscountOnAnvil(CallbackInfo ci) {
        Player player = this.player;
        if (player == null) return;

        player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
            if (cap.isNodeUnlocked("god_b", 1)) {
                int currentCost = this.cost.get();
                if (currentCost > 0) {
                    int discounted = Math.max(1, (int) Math.ceil(currentCost * 0.65D));
                    this.cost.set(discounted);
                }
            }
        });
    }
}