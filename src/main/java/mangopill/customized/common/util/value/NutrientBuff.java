package mangopill.customized.common.util.value;

import mangopill.customized.common.registry.ModEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import static mangopill.customized.common.CustomizedConfig.*;

public enum NutrientBuff {
    ICED(ModEffectRegistry.ICED, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),
    WARM_STOMACH(ModEffectRegistry.WARM_STOMACH, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),

    VITALITY(ModEffectRegistry.VITALITY, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    ANTIDOTE(ModEffectRegistry.ANTIDOTE, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    SOAR(ModEffectRegistry.SOAR, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),

    SATURATION(MobEffects.SATURATION, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    METABOLISM(ModEffectRegistry.METABOLISM, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    ROBUST(ModEffectRegistry.ROBUST, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    VITALITY_RESTORATION(ModEffectRegistry.VITALITY_RESTORATION, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    CALORIE_BURN(ModEffectRegistry.CALORIE_BURN, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SMOOTH_BLOOD_FLOW(ModEffectRegistry.SMOOTH_BLOOD_FLOW, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    MENTAL_STIMULATION(ModEffectRegistry.MENTAL_STIMULATION, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    APPETITE_BOOST(ModEffectRegistry.APPETITE_BOOST, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SUSTAINED_ENERGY(ModEffectRegistry.SUSTAINED_ENERGY, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    STRESS_RELIEF(ModEffectRegistry.STRESS_RELIEF, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    HYDRATION_AND_PLUMPNESS(ModEffectRegistry.HYDRATION_AND_PLUMPNESS, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get());

    private final Holder<MobEffect> effect;
    private final double duration;
    private final double probability;

    NutrientBuff(Holder<MobEffect> effect, double duration, double probability) {
        this.effect = effect;
        this.duration = duration;
        this.probability = probability;
    }

    public Holder<MobEffect> getEffect() {
        return effect;
    }

    public double getDuration() {
        return duration;
    }

    public double getProbability() {
        return probability;
    }
}
