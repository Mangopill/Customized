package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 *This MobEffect can remove harmful effect from the entity and convert the harmful effect into health points.
 */
public class AntidoteEffect extends ModMobEffect {

    public AntidoteEffect(int color) {
        super(color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        for (MobEffectInstance effectInstance : livingEntity.getActiveEffects()) {
            if (effectInstance.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)) {
                if (livingEntity.getHealth() < livingEntity.getMaxHealth()){
                    livingEntity.heal((amplifier + 1) * 2.0F);
                }
                livingEntity.removeEffect(effectInstance.getEffect());
            }
        }
    }
}
