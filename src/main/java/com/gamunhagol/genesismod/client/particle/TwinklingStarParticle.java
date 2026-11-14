package com.gamunhagol.genesismod.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TwinklingStarParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private static final int[] FRAME_SEQUENCE = {0, 1, 2};
    private final int ticksPerFrame = 5;

    protected TwinklingStarParticle(ClientLevel level, double x, double y, double z,
                                    double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.lifetime = 80;
        this.gravity = 0.0F;
        this.hasPhysics = false;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        updateSpriteFrame();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            updateSpriteFrame();
        }
    }

    private void updateSpriteFrame() {
        int frameIndex = (this.age / this.ticksPerFrame) % FRAME_SEQUENCE.length;
        int targetSprite = FRAME_SEQUENCE[frameIndex];

        this.setSprite(this.sprites.get(targetSprite, 2));
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
            return new TwinklingStarParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
