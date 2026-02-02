package com.gamunhagol.genesismod.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class GreenFlameParticle extends TextureSheetParticle {

    protected GreenFlameParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);

        // 크기 및 수명 설정 (바닐라 불꽃 설정값)
        this.quadSize *= 0.55F; // 크기 조절
        this.lifetime = 24;     // 수명

        // 이렇게 하면 사방으로 퍼지는 현상이 원천 차단됩니다.
        this.xd = 0.0D;
        this.zd = 0.0D;

        // Y 속도(위로 올라가는 속도)만 아주 살짝 남겨둡니다.
        this.yd = vy;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        // OPAQUE는 투명 배경을 검은색으로 만듭니다.
        // 불꽃처럼 테두리가 투명해야 한다면 TRANSLUCENT를 써야 합니다.
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        // 15(최대 밝기)를 반환하여 어두운 곳에서도 선명하게 보임
        return 15728880;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            // [수정] 움직임을 처리하기 전에 속도를 제어합니다.
            // X, Z는 아예 안 움직이게 0으로 유지하고
            this.xd = 0;
            this.zd = 0;

            // 위치 업데이트
            this.move(this.xd, this.yd, this.zd);

            // 마찰력: 위로 올라가는 힘만 서서히 줄어들게 합니다.
            this.yd *= 0.86F;
        }
    }
    // 팩토리: 이 파티클을 생성하는 공장
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            GreenFlameParticle particle = new GreenFlameParticle(level, x, y, z, vx, vy, vz);

            particle.pickSprite(this.spriteSet);

            return particle;
        }
    }
}
