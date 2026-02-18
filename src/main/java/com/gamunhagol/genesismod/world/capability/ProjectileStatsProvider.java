package com.gamunhagol.genesismod.world.capability;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectileStatsProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<ProjectileStats> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final ProjectileStats backend = new ProjectileStats();
    private final LazyOptional<ProjectileStats> optional = LazyOptional.of(() -> backend);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }

    // 내부 클래스: 실제 데이터 저장 로직
    // [중요] 외부에서 등록(Register)할 수 있도록 static이어야 합니다.
    public static class ProjectileStats {
        private DamageSnapshot snapshot = DamageSnapshot.EMPTY;

        public void setSnapshot(DamageSnapshot snapshot) {
            this.snapshot = snapshot;
        }

        public DamageSnapshot getSnapshot() {
            return snapshot;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("phys", snapshot.physical());
            tag.putFloat("magic", snapshot.magic());
            tag.putFloat("fire", snapshot.fire());
            tag.putFloat("light", snapshot.lightning());
            tag.putFloat("frost", snapshot.frost());
            tag.putFloat("holy", snapshot.holy());
            tag.putFloat("dest", snapshot.destruction());
            return tag;
        }

        public void deserializeNBT(CompoundTag tag) {
            this.snapshot = new DamageSnapshot(
                    tag.getFloat("phys"), tag.getFloat("magic"), tag.getFloat("fire"),
                    tag.getFloat("light"), tag.getFloat("frost"), tag.getFloat("holy"),
                    tag.getFloat("dest")
            );
        }
    }
}