package com.gamunhagol.genesismod.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResistanceBlessingParticle extends TextureSheetParticle {
    private final float initialSize = 0.05F;
    private final float targetSize = 0.25F;

    protected ResistanceBlessingParticle(ClientLevel level, double x, double y, double z,
                                         double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.lifetime = 12;
        this.gravity = 0.0F;
        this.hasPhysics = false;

        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;

        this.quadSize = this.initialSize;
        this.alpha = 1.0F;

        this.pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float progress = (float) this.age / (float) this.lifetime;

        float easeOut = 1.0F - (1.0F - progress) * (1.0F - progress);
        this.quadSize = this.initialSize + (this.targetSize - this.initialSize) * easeOut;

        if (progress > 0.8F) {
            float fadeProgress = (progress - 0.5F) / 0.5F;
            this.alpha = Math.max(0.0F, 1.0F - fadeProgress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ResistanceBlessingParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
