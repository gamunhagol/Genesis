package com.gamunhagol.genesismod.world.capability;

public class GenesisWeaponStats implements IGenesisWeaponStats {
    private float holyDamage;
    private float destructionDamage;

    @Override
    public float getHolyDamage() { return this.holyDamage; }

    @Override
    public void setHolyDamage(float damage) { this.holyDamage = damage; }

    @Override
    public float getDestructionDamage() { return this.destructionDamage; }

    @Override
    public void setDestructionDamage(float damage) { this.destructionDamage = damage; }

    @Override
    public void copyFrom(IGenesisWeaponStats source) {
        this.holyDamage = source.getHolyDamage();
        this.destructionDamage = source.getDestructionDamage();
    }
}
