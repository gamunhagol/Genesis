package com.gamunhagol.genesismod.world.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

public class GenericScrollItem extends AbstractScrollItem {
    private final BiConsumer<Level, ServerPlayer> effect;

    public GenericScrollItem(Properties properties, float fpCost, int cooldownSeconds, BiConsumer<Level, ServerPlayer> effect) {
        // 부모 생성자 호출 (초 단위를 틱으로 변환해서 전달)
        super(properties, fpCost, cooldownSeconds * 20);
        this.effect = effect;
    }

    @Override
    protected void executeEffect(Level level, ServerPlayer player) {
        // 부모가 "성공했다!"고 판단할 때만 이 메서드를 실행함
        this.effect.accept(level, player);
    }
}