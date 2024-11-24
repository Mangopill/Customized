package mangopill.customized.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SteamParticle extends TextureSheetParticle {

    public SteamParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.scale(2.0F);
        this.setSize(0.5F, 0.5F);
        this.lifetime = this.random.nextInt(35) + 35;
        this.gravity = this.random.nextFloat() + 0.2F;;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime) {
            this.xd *= this.random.nextBoolean() ? 1.0F : -1.0F;
            this.zd *= this.random.nextBoolean() ? 1.0F : -1.0F;
            this.yd *= this.gravity;
            float dampingFactor = 1.0F - (this.age / (float) this.lifetime);
            this.xd *= dampingFactor;
            this.zd *= dampingFactor;
            this.yd *= dampingFactor;
            this.move(this.xd, this.yd, this.zd);
            if (this.alpha > 0.02F) {
                this.alpha -= 0.02F;
            }
        } else {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SteamParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public SteamParticleProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SteamParticle particle = new SteamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setAlpha(0.7F);
            particle.pickSprite(sprite);
            return particle;
        }
    }
}
