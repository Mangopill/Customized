package mangopill.customized.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DirtParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    protected DirtParticle(ClientLevel level, double posX, double posY, double posZ, SpriteSet spriteSet) {
        super(level, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.spriteSet = spriteSet;
        this.gravity = 1;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class DirtParticleProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public DirtParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DirtParticle(level, x, y, z, spriteSet);
        }
    }
}
