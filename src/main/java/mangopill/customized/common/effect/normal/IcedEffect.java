package mangopill.customized.common.effect.normal;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.world.entity.LivingEntity;
/**
 *This MobEffect can extinguish the fire on the entity when it is no longer in contact with a fire source.
 */
public class IcedEffect extends CMobEffect {

    public IcedEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide && livingEntity.isOnFire()) {
            livingEntity.clearFire();
        }
        return true;
    }
}
