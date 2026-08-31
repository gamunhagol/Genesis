package com.gamunhagol.genesismod.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    private float mental = 20.0f;
    private float maxMental = 20.0f;
    private float regenRate = 0.005f;

    private boolean isDirty = false;

    // Blessing 패시브 상태 추적 변수
    private int windDashCooldown = 0;
    private int iceIdleTicks = 0;
    private int forestSneakTicks = 0;
    private int deathEvasionCooldown = 0; // 사망 회피 쿨타임 (18해금 보상)

    private final Set<String> learnedSpells = new LinkedHashSet<>();
    private final Set<String> unlockedNodes = new LinkedHashSet<>();

    private final Map<UUID, Float> activeSummons = new HashMap<>();

    public void tick() {
        float totalUpkeep = getTotalUpkeep();

        if (this.mental < this.maxMental || totalUpkeep > 0) {
            this.mental += (this.regenRate - totalUpkeep);

            if (this.mental > this.maxMental) {
                this.mental = this.maxMental;
            }
            if (this.mental <= 0.0f) {
                this.mental = 0.0f;
            }
            this.isDirty = true;
        }

        if (this.windDashCooldown > 0) {
            this.windDashCooldown--;
            this.isDirty = true;
        }

        if (this.deathEvasionCooldown > 0) {
            this.deathEvasionCooldown--;
            this.isDirty = true;
        }
    }

    public void updateMaxMental() {
        this.maxMental = 2.0f + (this.getMind() - 1) * 2.02f;
        this.isDirty = true;
    }

    public void learnSpell(String spellId) {
        if (learnedSpells.add(spellId)) {
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

    public void resetNode(String statueId, int nodeId) {
        String key = statueId + "_" + nodeId;
        if (unlockedNodes.remove(key)) {
            this.setDirty(true);
        }
    }

    public void resetStatueAllNodes(String statueId) {
        boolean removedAny = unlockedNodes.removeIf(key -> key.startsWith(statueId + "_"));
        if (removedAny) {
            this.setDirty(true);
        }
    }

    public void resetAllNodes() {
        if (!unlockedNodes.isEmpty()) {
            unlockedNodes.clear();
            this.setDirty(true);
        }
    }

    public Set<String> getValidDedications() {
        Set<String> activeGods = new LinkedHashSet<>();

        for (String key : unlockedNodes) {
            activeGods.add(key.substring(0, key.lastIndexOf('_')));
        }

        if (activeGods.isEmpty()) {
            return Collections.emptySet();
        }

        if (activeGods.size() == 1) {
            return activeGods;
        }

        if (activeGods.size() == 2 && activeGods.contains("god_g") && activeGods.contains("god_h")) {
            return activeGods;
        }

        return Collections.emptySet();
    }

    public int getUnlockedNodeCount(String statueId) {
        int count = 0;
        for (String key : unlockedNodes) {
            if (key.startsWith(statueId + "_")) {
                count++;
            }
        }
        return count;
    }

    public int getMaxNodeCount() {
        Set<String> validGods = getValidDedications();

        if (validGods.isEmpty()) {
            return 0;
        }

        int max = 0;
        for (String god : validGods) {
            max = Math.max(max, getUnlockedNodeCount(god));
        }
        return max;
    }

    public int getBonusStat() {
        return getMaxNodeCount() >= 11 ? 3 : 0;
    }

    public int getSpellCapacityBonus() {
        return getMaxNodeCount() >= 6 ? 5 : 0;
    }

    public boolean hasSpell(String spellId) {
        return learnedSpells.contains(spellId);
    }

    public Set<String> getLearnedSpells() {
        return learnedSpells;
    }

    public void addSummon(UUID id, float cost) {
        this.activeSummons.put(id, cost);
        this.setDirty(true);
    }

    public void removeSummon(UUID id) {
        if (this.activeSummons.remove(id) != null) {
            this.setDirty(true);
        }
    }

    public Map<UUID, Float> getActiveSummons() {
        return this.activeSummons;
    }

    public float getTotalUpkeep() {
        float total = 0.0f;
        for (float cost : this.activeSummons.values()) {
            total += cost;
        }
        return total;
    }

    // Getter & Setter
    public int getVigor() { return vigor + getBonusStat(); }
    public int getRawVigor() { return vigor; }
    public void setVigor(int v) { if(vigor != v) { vigor = v; isDirty = true; } }

    public int getMind() { return mind + getBonusStat(); }
    public int getRawMind() { return mind; }
    public void setMind(int v) { if(mind != v) { mind = v; isDirty = true; updateMaxMental(); } }

    public int getEndurance() { return endurance + getBonusStat(); }
    public int getRawEndurance() { return endurance; }
    public void setEndurance(int v) { if(endurance != v) { endurance = v; isDirty = true; } }

    public int getStrength() { return strength + getBonusStat(); }
    public int getRawStrength() { return strength; }
    public void setStrength(int v) { if(strength != v) { strength = v; isDirty = true; } }

    public int getDexterity() { return dexterity + getBonusStat(); }
    public int getRawDexterity() { return dexterity; }
    public void setDexterity(int v) { if(dexterity != v) { dexterity = v; isDirty = true; } }

    public int getIntelligence() { return intelligence + getBonusStat(); }
    public int getRawIntelligence() { return intelligence; }
    public void setIntelligence(int v) { if(intelligence != v) { intelligence = v; isDirty = true; } }

    public int getFaith() { return faith + getBonusStat(); }
    public int getRawFaith() { return faith; }
    public void setFaith(int v) { if(faith != v) { faith = v; isDirty = true; } }

    public int getArcane() { return arcane + getBonusStat(); }
    public int getRawArcane() { return arcane; }
    public void setArcane(int v) { if(arcane != v) { arcane = v; isDirty = true; } }

    public float getMental() { return mental; }
    public void setMental(float v) { if(Math.abs(mental - v) > 0.001f) { mental = v; isDirty = true; } }
    public float getMaxMental() { return maxMental; }
    public void setMaxMental(float v) { if(Math.abs(maxMental - v) > 0.001f) { maxMental = v; isDirty = true; } }

    public boolean isLevelUpUnlocked() { return isLevelUpUnlocked; }
    public void setLevelUpUnlocked(boolean v) {
        if(this.isLevelUpUnlocked != v) {
            this.isLevelUpUnlocked = v;
            this.setDirty(true);
        }
    }

    public boolean isDirty() { return isDirty; }
    public void setDirty(boolean d) { this.isDirty = d; }

    // 축복 상태 Getter & Setter
    public int getWindDashCooldown() { return windDashCooldown; }
    public void setWindDashCooldown(int ticks) {
        this.windDashCooldown = ticks;
        this.isDirty = true;
    }

    public int getIceIdleTicks() { return iceIdleTicks; }
    public void setIceIdleTicks(int ticks) { this.iceIdleTicks = ticks; }

    public int getForestSneakTicks() { return forestSneakTicks; }
    public void setForestSneakTicks(int ticks) { this.forestSneakTicks = ticks; }

    public int getDeathEvasionCooldown() { return deathEvasionCooldown; }
    public void setDeathEvasionCooldown(int ticks) {
        this.deathEvasionCooldown = ticks;
        this.isDirty = true;
    }

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
        this.activeSummons.clear();
        this.activeSummons.putAll(source.activeSummons);

        this.windDashCooldown = source.windDashCooldown;
        this.iceIdleTicks = source.iceIdleTicks;
        this.forestSneakTicks = source.forestSneakTicks;
        this.deathEvasionCooldown = source.deathEvasionCooldown;

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

        nbt.putInt("windDashCooldown", windDashCooldown);
        nbt.putInt("deathEvasionCooldown", deathEvasionCooldown);

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

        ListTag summonsTag = new ListTag();
        for (Map.Entry<UUID, Float> entry : activeSummons.entrySet()) {
            CompoundTag summonNbt = new CompoundTag();
            summonNbt.putUUID("id", entry.getKey());
            summonNbt.putFloat("cost", entry.getValue());
            summonsTag.add(summonNbt);
        }
        nbt.put("activeSummons", summonsTag);

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

        if (nbt.contains("windDashCooldown")) {
            windDashCooldown = nbt.getInt("windDashCooldown");
        }

        if (nbt.contains("deathEvasionCooldown")) {
            deathEvasionCooldown = nbt.getInt("deathEvasionCooldown");
        }

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

        activeSummons.clear();
        if (nbt.contains("activeSummons", Tag.TAG_LIST)) {
            ListTag summonsTag = nbt.getList("activeSummons", Tag.TAG_COMPOUND);
            for (int i = 0; i < summonsTag.size(); i++) {
                CompoundTag summonNbt = summonsTag.getCompound(i);
                activeSummons.put(summonNbt.getUUID("id"), summonNbt.getFloat("cost"));
            }
        }
    }

    public Set<String> getUnlockedNodes() {
        return unlockedNodes;
    }
}