package com.gamunhagol.genesismod.world.item.weapon;

public enum GreatBowTier {
    NORMAL("great_bow", 484, "great_bow_preset"),
    ROOT_WOVEN("root_woven_bow", 524, "great_bow_preset"),
    ELVENIA("elvenia_great_bow", 500, "great_bow_preset"),
    ANCIENT_ELVENIA("ancient_elvenia_great_bow", 3121, "great_bow_preset"),
    PEWRIESE("pewriese_great_bow", 4431, "great_bow_preset");

    private final String registryName;
    private final int durability;
    private final String epicFightPreset;

    GreatBowTier(String registryName, int durability, String epicFightPreset) {
        this.registryName = registryName;
        this.durability = durability;
        this.epicFightPreset = epicFightPreset;
    }

    public String getRegistryName() { return registryName; }
    public int getDurability() { return durability; }
    public String getEpicFightPreset() { return epicFightPreset; }
}
