package com.gamunhagol.genesismod.world.capability;

public interface IGenesisWeaponStats {
    float getHolyDamage();
    void setHolyDamage(float damage);

    float getDestructionDamage();
    void setDestructionDamage(float damage);

    void copyFrom(IGenesisWeaponStats source);
}
