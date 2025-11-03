package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
/**
 *This MobEffect can remove harmful effect from the entity and convert the harmful effect into health points.
 */
public class AntidoteEffect extends CMobEffect {

    public AntidoteEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        for (MobEffectInstance effectInstance : livingEntity.getActiveEffects()) {
            if (effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL)) {
                if (livingEntity.getHealth() < livingEntity.getMaxHealth()){
                    livingEntity.heal((amplifier + 1) * 2.0F);
                }
                livingEntity.removeEffect(effectInstance.getEffect());
            }
        }
        return true;
    }
}
