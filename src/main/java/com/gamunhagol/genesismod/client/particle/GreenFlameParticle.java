package com.gamunhagol.genesismod.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class GreenFlameParticle extends TextureSheetParticle {

    protected GreenFlameParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);

        // 크기 및 수명 설정 (바닐라 불꽃 설정값)
        this.quadSize *= 0.45F; // 크기 조절
        this.lifetime = 10 + level.random.nextInt(12);   // 수명

        // [수정] 생성 시점에 아주 미세한 수평 속도(흔들림)를 부여합니다.
        // vx, vz가 0으로 들어와도 자체적으로 미세하게 퍼지도록 설정
        this.xd = vx + (level.random.nextFloat() - level.random.nextFloat()) * 0.01F;
        this.zd = vz + (level.random.nextFloat() - level.random.nextFloat()) * 0.01F;

        // 위로 올라가는 속도 (바닐라와 유사하게)
        this.yd = vy;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
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
            // [수정] xd, zd를 0으로 고정하던 코드를 삭제했습니다.
            // 대신 위로 올라가면서 옆으로 아주 살짝씩 흔들리게 하고 싶다면 아래 주석을 해제하세요.
            // this.xd += (this.random.nextFloat() - this.random.nextFloat()) * 0.005F;

            // 위치 업데이트 (이제 xd, yd, zd가 모두 반영됩니다)
            this.move(this.xd, this.yd, this.zd);

            // [수정] 속도 감쇠 (공기 저항)
            // 수평 속도는 서서히 줄어들어 분수처럼 퍼지지 않게 하고,
            // 수직 속도(yd)는 유지하거나 살짝 줄여서 위로 계속 가게 합니다.
            this.xd *= 0.86F;
            this.zd *= 0.86F;
            this.yd *= 0.86F;

            // 바닥에 닿았을 때의 처리 (양초 위이므로 보통은 생략 가능)
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
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
