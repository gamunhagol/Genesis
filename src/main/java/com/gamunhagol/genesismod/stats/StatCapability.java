package com.gamunhagol.genesismod.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class StatCapability implements INBTSerializable<CompoundTag> {
    private int vigor = 10;
    private int mind = 10;
    private int endurance = 10;
    private int strength = 10;
    private int dexterity = 10;
    private int intelligence = 10;
    private int faith = 10;
    private int arcane = 9;

    private boolean isLevelUpUnlocked = false;

    // 초기 리소스 상태 (StatApplier에 의해 갱신되기 전 기본값)
    private float mental = 20.0f;
    private float maxMental = 20.0f;
    private float regenRate = 0.005f;

    private boolean isDirty = false;

    public void tick() {
        if (this.mental < this.maxMental) {
            this.mental = Math.min(this.maxMental, this.mental + this.regenRate);
            this.isDirty = true;
        }
    }

    public void updateMaxMental() {
        // 레벨 1일 때 2.0, 레벨 99일 때 약 200.0
        this.maxMental = 2.0f + (this.mind - 1) * 2.02f;
        this.isDirty = true;
    }

    // Getter & Setter
    public int getVigor() { return vigor; }
    public void setVigor(int v) { if(vigor != v) { vigor = v; isDirty = true; } }
    public int getMind() { return mind; }
    public void setMind(int v) { if(mind != v) { mind = v; isDirty = true; updateMaxMental(); } }
    public int getEndurance() { return endurance; }
    public void setEndurance(int v) { if(endurance != v) { endurance = v; isDirty = true; } }
    public int getStrength() { return strength; }
    public void setStrength(int v) { if(strength != v) { strength = v; isDirty = true; } }
    public int getDexterity() { return dexterity; }
    public void setDexterity(int v) { if(dexterity != v) { dexterity = v; isDirty = true; } }
    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int v) { if(intelligence != v) { intelligence = v; isDirty = true; } }
    public int getFaith() { return faith; }
    public void setFaith(int v) { if(faith != v) { faith = v; isDirty = true; } }
    public int getArcane() { return arcane; }
    public void setArcane(int v) { if(arcane != v) { arcane = v; isDirty = true; } }

    public float getMental() { return mental; }
    public void setMental(float v) { if(Math.abs(mental - v) > 0.001f) { mental = v; isDirty = true; } }
    public float getMaxMental() { return maxMental; }
    public void setMaxMental(float v) { if(Math.abs(maxMental - v) > 0.001f) { maxMental = v; isDirty = true; } }

    public boolean isLevelUpUnlocked() { return isLevelUpUnlocked; }
    public void setLevelUpUnlocked(boolean v) {
        if(this.isLevelUpUnlocked != v) {
            this.isLevelUpUnlocked = v;
            this.setDirty(true); // 데이터 변경 알림 [cite: 2026-02-15]
        }
    }

    public boolean isDirty() { return isDirty; }
    public void setDirty(boolean d) { this.isDirty = d; }

    public void copyFrom(StatCapability source) {
        this.vigor = source.vigor; this.mind = source.mind; this.endurance = source.endurance;
        this.strength = source.strength; this.dexterity = source.dexterity;
        this.intelligence = source.intelligence; this.faith = source.faith; this.arcane = source.arcane;
        this.mental = source.mental; this.maxMental = source.maxMental;
        this.isLevelUpUnlocked = source.isLevelUpUnlocked;
        this.isDirty = true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("isLevelUpUnlocked", isLevelUpUnlocked);
        nbt.putInt("vigor", vigor); nbt.putInt("mind", mind); nbt.putInt("endurance", endurance);
        nbt.putInt("strength", strength); nbt.putInt("dexterity", dexterity);
        nbt.putInt("intelligence", intelligence); nbt.putInt("faith", faith); nbt.putInt("arcane", arcane);
        nbt.putFloat("mental", mental); nbt.putFloat("maxMental", maxMental);
        nbt.putBoolean("isLevelUpUnlocked", isLevelUpUnlocked);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        vigor = nbt.getInt("vigor"); mind = nbt.getInt("mind"); endurance = nbt.getInt("endurance");
        strength = nbt.getInt("strength"); dexterity = nbt.getInt("dexterity");
        intelligence = nbt.getInt("intelligence"); faith = nbt.getInt("faith");
        arcane = nbt.getInt("arcane"); mental = nbt.getFloat("mental");
        maxMental = nbt.getFloat("maxMental");
        isLevelUpUnlocked = nbt.getBoolean("isLevelUpUnlocked");
    }
}