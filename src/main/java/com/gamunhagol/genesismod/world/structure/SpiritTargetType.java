package com.gamunhagol.genesismod.world.structure;

public enum SpiritTargetType {
    ANCIENT_CITY("minecraft:ancient_city");

    private final String id;
    SpiritTargetType(String id) {this.id = id; }
    public String id() {return id; }
}
