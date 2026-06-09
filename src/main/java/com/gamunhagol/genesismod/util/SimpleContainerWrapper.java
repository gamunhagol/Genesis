package com.gamunhagol.genesismod.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SimpleContainerWrapper extends SimpleContainer {
    private final IItemHandler handler;

    public SimpleContainerWrapper(IItemHandler handler) {
        super(handler.getSlots());
        this.handler = handler;

        for (int i = 0; i < handler.getSlots(); i++) {
            super.setItem(i, handler.getStackInSlot(i).copy());
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        syncToHandler(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        syncToHandler(slot);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = super.removeItemNoUpdate(slot);
        syncToHandler(slot);
        return stack;
    }

    private void syncToHandler(int slot) {
        if (this.handler instanceof IItemHandlerModifiable modifiable) {
            modifiable.setStackInSlot(slot, this.getItem(slot));
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
