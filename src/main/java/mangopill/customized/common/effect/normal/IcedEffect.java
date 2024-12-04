package mangopill.customized.common.effect.normal;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
/**
 *This MobEffect can extinguish the fire on the entity when it is no longer in contact with a fire source.
 */
public class IcedEffect extends ModMobEffect {

    public IcedEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide && livingEntity.isOnFire()) {
            livingEntity.clearFire();
        }
        return true;
    }
}
