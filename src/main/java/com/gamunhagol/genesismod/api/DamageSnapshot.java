package com.gamunhagol.genesismod.api;

public record DamageSnapshot(
        float physical, float magic, float fire,
        float lightning, float frost, float holy, float destruction
) {
    public static final DamageSnapshot EMPTY = new DamageSnapshot(0, 0, 0, 0, 0, 0, 0);

    public boolean isEmpty() {
        return physical == 0 && magic == 0 && fire == 0 &&
                lightning == 0 && frost == 0 && holy == 0 && destruction == 0;
    }
}