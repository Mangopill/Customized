package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 * This MobEffect can accelerate the player's health regeneration.
 */
public class StressReliefEffect extends ModMobEffect implements ShrinkNutritionMobEffect, ShrinkSaturationMobEffect, CombinationMobEffect {

    public StressReliefEffect(int color) {
        super(color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (80 / Math.min(80 , amplifier + 1)) == 0;
    }

    @Override
    public float getShrinkNutritionModifier() {
        return -0.05F;
    }

    @Override
    public float getShrinkSaturationModifier() {
        return -0.05F;
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(BITTER, NUMBING));
    }
}
