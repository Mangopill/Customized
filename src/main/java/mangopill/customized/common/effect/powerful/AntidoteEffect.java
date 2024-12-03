package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AntidoteEffect extends ModMobEffect {
    /**
     *This MobEffect can remove harmful effect from the entity and convert the harmful effect into health points.
     */
    public AntidoteEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        for (MobEffectInstance effectInstance : livingEntity.getActiveEffects()) {
            if (effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL)) {
                if (livingEntity.getHealth() < livingEntity.getMaxHealth()){
                    livingEntity.heal(2.0F);
                }
                livingEntity.removeEffect(effectInstance.getEffect());
            }
        }
        return true;
    }
}
