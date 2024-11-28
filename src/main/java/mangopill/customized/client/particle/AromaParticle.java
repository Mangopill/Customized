package mangopill.customized.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class AromaParticle extends TextureSheetParticle {

    public AromaParticle(ClientLevel level, double x, double y, double z, double ySpeed) {
        super(level, x, y, z, 0.0, ySpeed, 0.0);

        this.setSize(0.3F, 0.3F); // 设置粒子尺寸
        this.scale(1.0F); // 设置粒子初始大小
        this.lifetime = this.random.nextInt(40) + 40;
        this.gravity = 0.0F; // 不受重力影响
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
            double speedVariationY = (this.random.nextFloat() - 0.5) * 0.02;
            this.yd += speedVariationY;
            this.yd *= 0.98F;
            float alphaFactor = 1.0F - (this.age / (float) this.lifetime);
            this.alpha = Math.max(0.0F, alphaFactor * 0.8F);
            this.move(0.0, this.yd, 0.0);
        } else {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AromaParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public AromaParticleProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AromaParticle particle = new AromaParticle(level, x, y, z, ySpeed);
            particle.setAlpha(0.8F);
            particle.pickSprite(sprite);
            return particle;
        }
    }
}
