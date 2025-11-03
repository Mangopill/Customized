package mangopill.customized.common.util.value;

import mangopill.customized.common.registry.CEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import static mangopill.customized.common.CustomizedConfig.*;

public enum NutrientBuff {
    ICED(CEffectRegistry.ICED, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),
    WARM_STOMACH(CEffectRegistry.WARM_STOMACH, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),

    VITALITY(CEffectRegistry.VITALITY, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    ANTIDOTE(CEffectRegistry.ANTIDOTE, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    SOAR(CEffectRegistry.SOAR, POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),

    NOCTENERGY_SURGE(CEffectRegistry.NOCTENERGY_SURGE, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    METABOLISM(CEffectRegistry.METABOLISM, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    ROBUST(CEffectRegistry.ROBUST, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    VITALITY_RESTORATION(CEffectRegistry.VITALITY_RESTORATION, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    CALORIE_BURN(CEffectRegistry.CALORIE_BURN, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SMOOTH_BLOOD_FLOW(CEffectRegistry.SMOOTH_BLOOD_FLOW, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    MENTAL_STIMULATION(CEffectRegistry.MENTAL_STIMULATION, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    APPETITE_BOOST(CEffectRegistry.APPETITE_BOOST, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SUSTAINED_ENERGY(CEffectRegistry.SUSTAINED_ENERGY, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    STRESS_RELIEF(CEffectRegistry.STRESS_RELIEF, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    HYDRATION_AND_PLUMPNESS(CEffectRegistry.HYDRATION_AND_PLUMPNESS, COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get());

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
