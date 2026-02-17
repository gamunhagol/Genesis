package com.gamunhagol.genesismod.world.capability;

public class GenesisWeaponStats implements IGenesisWeaponStats {
    private float holyDamage;
    private float destructionDamage;

    private int reinforceLevel = 0;

    @Override
    public float getHolyDamage() { return this.holyDamage; }

    @Override
    public void setHolyDamage(float damage) { this.holyDamage = damage; }

    @Override
    public float getDestructionDamage() { return this.destructionDamage; }

    @Override
    public void setDestructionDamage(float damage) { this.destructionDamage = damage; }

    @Override
    public int getReinforceLevel() { return this.reinforceLevel; }

    @Override
    public void setReinforceLevel(int level) { this.reinforceLevel = level; }

    @Override
    public void copyFrom(IGenesisWeaponStats source) {
        this.holyDamage = source.getHolyDamage();
        this.destructionDamage = source.getDestructionDamage();
        this.reinforceLevel = source.getReinforceLevel();
    }
}
