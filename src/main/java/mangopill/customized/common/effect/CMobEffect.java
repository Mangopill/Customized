package mangopill.customized.common.effect;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;

public abstract class CMobEffect extends MobEffect {
    public CMobEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
