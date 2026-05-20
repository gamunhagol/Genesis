package com.gamunhagol.genesismod.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.LinkedHashSet;
import java.util.Set;

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

    private final Set<String> learnedSpells = new LinkedHashSet<>();
    private final Set<String> unlockedNodes = new LinkedHashSet<>();

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

    public void learnSpell(String spellId) {
        if (learnedSpells.add(spellId)) { // 새로운 마법이면 true 반환
            this.setDirty(true);
        }
    }

    public void unlockNode(String statueId, int nodeId) {
        String key = statueId + "_" + nodeId;
        if (unlockedNodes.add(key)) {
            this.setDirty(true);
        }
    }

    public boolean isNodeUnlocked(String statueId, int nodeId) {
        return unlockedNodes.contains(statueId + "_" + nodeId);
    }

    public boolean hasSpell(String spellId) {
        return learnedSpells.contains(spellId);
    }

    public Set<String> getLearnedSpells() {
        return learnedSpells;
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
        this.learnedSpells.clear();
        this.learnedSpells.addAll(source.learnedSpells);
        this.unlockedNodes.clear();
        this.unlockedNodes.addAll(source.unlockedNodes);

        this.isDirty = true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("vigor", vigor);
        nbt.putInt("mind", mind);
        nbt.putInt("endurance", endurance);
        nbt.putInt("strength", strength);
        nbt.putInt("dexterity", dexterity);
        nbt.putInt("intelligence", intelligence);
        nbt.putInt("faith", faith);
        nbt.putInt("arcane", arcane);
        nbt.putFloat("mental", mental);
        nbt.putFloat("maxMental", maxMental);
        nbt.putBoolean("isLevelUpUnlocked", isLevelUpUnlocked);

        // 마법 목록을 ListTag로 변환하여 저장
        ListTag spellsTag = new ListTag();
        for (String id : learnedSpells) {
            spellsTag.add(StringTag.valueOf(id));
        }
        nbt.put("learnedSpells", spellsTag);

        ListTag nodesTag = new ListTag();
        for (String key : unlockedNodes) {
            nodesTag.add(StringTag.valueOf(key));
        }
        nbt.put("unlockedNodes", nodesTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        vigor = nbt.getInt("vigor");
        mind = nbt.getInt("mind");
        endurance = nbt.getInt("endurance");
        strength = nbt.getInt("strength");
        dexterity = nbt.getInt("dexterity");
        intelligence = nbt.getInt("intelligence");
        faith = nbt.getInt("faith");
        arcane = nbt.getInt("arcane");
        mental = nbt.getFloat("mental");
        maxMental = nbt.getFloat("maxMental");
        isLevelUpUnlocked = nbt.getBoolean("isLevelUpUnlocked");

        // 마법 목록 불러오기
        learnedSpells.clear();
        if (nbt.contains("learnedSpells", Tag.TAG_LIST)) {
            ListTag spellsTag = nbt.getList("learnedSpells", Tag.TAG_STRING);
            for (int i = 0; i < spellsTag.size(); i++) {
                learnedSpells.add(spellsTag.getString(i));
            }
        }
        unlockedNodes.clear();
        if (nbt.contains("unlockedNodes", Tag.TAG_LIST)) {
            ListTag nodesTag = nbt.getList("unlockedNodes", Tag.TAG_STRING);
            for (int i = 0; i < nodesTag.size(); i++) {
                unlockedNodes.add(nodesTag.getString(i));
            }
        }
    }
    public Set<String> getUnlockedNodes() {
        return unlockedNodes;
    }
}