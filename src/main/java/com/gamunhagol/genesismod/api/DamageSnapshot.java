package com.gamunhagol.genesismod.api;

/**
 * 공격 시점의 대미지 정보를 저장하는 불변 객체.
 * 1.20.1의 Record 기능을 활용하여 간결하게 작성.
 */
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