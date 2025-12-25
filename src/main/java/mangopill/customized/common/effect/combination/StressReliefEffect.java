package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.world.entity.LivingEntity;

/**
 * This MobEffect can accelerate the player's health regeneration.
 */
public class StressReliefEffect extends CMobEffect {

    public StressReliefEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (80 / Math.min(80 , amplifier + 1)) == 0;
    }
}
