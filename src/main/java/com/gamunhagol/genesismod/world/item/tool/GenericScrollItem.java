package com.gamunhagol.genesismod.world.item.tool;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

public class GenericScrollItem extends AbstractScrollItem {
    private final BiConsumer<Level, ServerPlayer> effect;

    public GenericScrollItem(Properties properties, float fpCost, int cooldownSeconds, BiConsumer<Level, ServerPlayer> effect) {
        super(properties, fpCost, cooldownSeconds * 20);
        this.effect = effect;
    }

    @Override
    protected void executeEffect(Level level, ServerPlayer player) {
        this.effect.accept(level, player);
    }
}