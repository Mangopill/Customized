package mangopill.customized.common.effect.normal;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class WarmStomachEffect extends MobEffect {
    public WarmStomachEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
    // This MobEffect has no effect on its own, its functionality is handled in the Mixin.
    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
