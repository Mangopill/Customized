package mangopill.customized.common.effect.normal;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 *This MobEffect can extinguish the fire on the entity when it is no longer in contact with a fire source.
 */
public class IcedEffect extends ModMobEffect {

    public IcedEffect(int color) {
        super(color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide && livingEntity.isOnFire()) {
            livingEntity.clearFire();
        }
    }
}
