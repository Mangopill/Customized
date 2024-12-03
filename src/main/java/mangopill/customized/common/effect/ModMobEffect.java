package mangopill.customized.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class ModMobEffect extends MobEffect {
    public ModMobEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
