package mangopill.customized.common.effect.powerful;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AntidoteEffect extends MobEffect {
    public AntidoteEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        for (MobEffectInstance effectInstance : livingEntity.getActiveEffects()) {
            if (effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL)) {
                if (!livingEntity.level().isClientSide){
                    livingEntity.heal(2.0F);
                }
                livingEntity.removeEffect(effectInstance.getEffect());
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
