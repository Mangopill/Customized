package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 * This MobEffect can enhance the entity's health regeneration.
 */
public class SmoothBloodFlowEffect extends ModMobEffect implements ShrinkSaturationMobEffect, ShrinkNutritionMobEffect, CombinationMobEffect {

    public SmoothBloodFlowEffect(int color) {
        super(color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(2.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (80 / (amplifier + 1)) == 0;
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(SALTY, NUMBING));
    }
}
