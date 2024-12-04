package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 *This MobEffect can give the player the HEAL effect.
 */
public class HydrationAndPlumpnessEffect extends ModMobEffect implements ShrinkNutritionMobEffect, ShrinkSaturationMobEffect, CombinationMobEffect {

    public HydrationAndPlumpnessEffect(int color) {
        super(color);
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.HEAL, 200, amplifier));
        }
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
        return List.of(Set.of(FRESH, SALTY));
    }
}
