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
 *This MobEffect can give the player the HEAL effect. On top of that, it will also continuously grant the WATER_BREATHING effect.
 */
public class HydrationAndPlumpnessEffect extends ModMobEffect implements ShrinkNutritionMobEffect, ShrinkSaturationMobEffect, CombinationMobEffect {

    public HydrationAndPlumpnessEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, (amplifier + 1) * 260, amplifier));
        }
        return true;
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.HEAL, (amplifier + 1) * 150, amplifier));
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
