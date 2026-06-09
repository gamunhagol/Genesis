package com.gamunhagol.genesismod.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class GreenFlameParticle extends TextureSheetParticle {

    protected GreenFlameParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);

        this.quadSize *= 0.45F;
        this.lifetime = 10 + level.random.nextInt(12);

        this.xd = vx + (level.random.nextFloat() - level.random.nextFloat()) * 0.01F;
        this.zd = vz + (level.random.nextFloat() - level.random.nextFloat()) * 0.01F;

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
            this.move(this.xd, this.yd, this.zd);

            this.xd *= 0.86F;
            this.zd *= 0.86F;
            this.yd *= 0.86F;

            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }
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
