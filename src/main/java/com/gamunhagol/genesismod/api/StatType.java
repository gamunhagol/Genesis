package com.gamunhagol.genesismod.api;

public enum StatType {
    VIGOR("vigor"),
    MIND("mind"),
    ENDURANCE("endurance"),
    STRENGTH("strength"),
    DEXTERITY("dexterity"),
    INTELLIGENCE("intelligence"),
    FAITH("faith"),
    ARCANE("arcane");

    private final String name;

    StatType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static StatType byName(String name) {
        for (StatType type : values()) {
            if (type.name.equals(name)) return type;
        }
        return null;
    }
}