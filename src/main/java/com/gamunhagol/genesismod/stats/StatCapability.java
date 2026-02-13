package com.gamunhagol.genesismod.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class StatCapability implements INBTSerializable<CompoundTag> {

    // 1. 변수 선언
    private float mentalPower;
    private float maxMentalPower = 20.0f; // 기본 최대치
    private float regenRate = 0.005f;       // 틱당 회복량 (0.05 * 20틱 = 초당 1 회복)

    // 2. 재생 로직 (에픽파이트의 tick과 유사)
    // 이 메서드는 외부 이벤트 핸들러에서 매 틱마다 호출될 것입니다.
    public void tick() {
        if (this.mentalPower < this.maxMentalPower) {
            this.mentalPower += this.regenRate;
            if (this.mentalPower > this.maxMentalPower) {
                this.mentalPower = this.maxMentalPower;
            }
        }
    }

    // 소모 로직 (스킬 사용 시 호출)
    public boolean consumeMentalPower(float amount) {
        if (this.mentalPower >= amount) {
            this.mentalPower -= amount;
            return true; // 사용 성공
        }
        return false; // 사용 실패 (부족함)
    }

    // Getter & Setter
    public float getMentalPower() { return mentalPower; }
    public void setMentalPower(float mentalPower) { this.mentalPower = mentalPower; }
    public float getMaxMentalPower() { return maxMentalPower; }
    public void setMaxMentalPower(float maxMentalPower) { this.maxMentalPower = maxMentalPower; }

    // 3. 데이터 저장 (서버 재시작시 유지)
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("mentalPower", this.mentalPower);
        nbt.putFloat("maxMentalPower", this.maxMentalPower);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.mentalPower = nbt.getFloat("mentalPower");
        // 최대치 정보가 저장되어 있다면 불러오기, 없으면 기본값 유지
        if (nbt.contains("maxMentalPower")) {
            this.maxMentalPower = nbt.getFloat("maxMentalPower");
        }
    }
}
