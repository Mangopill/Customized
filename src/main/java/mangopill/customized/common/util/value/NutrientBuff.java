package mangopill.customized.common.util.value;

import mangopill.customized.common.registry.ModEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;

import static mangopill.customized.common.CustomizedConfig.*;

public enum NutrientBuff {
    ICED(ModEffectRegistry.ICED.get(), NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),
    WARM_STOMACH(ModEffectRegistry.WARM_STOMACH.get(), NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),

    VITALITY(ModEffectRegistry.VITALITY.get(), POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    ANTIDOTE(ModEffectRegistry.ANTIDOTE.get(), POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),
    SOAR(ModEffectRegistry.SOAR.get(), POWERFUL_BUFF_DURATION.get(), POWERFUL_BUFF_PROBABILITY.get()),

    NOCTENERGY_SURGE(ModEffectRegistry.NOCTENERGY_SURGE.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    METABOLISM(ModEffectRegistry.METABOLISM.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    ROBUST(ModEffectRegistry.ROBUST.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    VITALITY_RESTORATION(ModEffectRegistry.VITALITY_RESTORATION.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    CALORIE_BURN(ModEffectRegistry.CALORIE_BURN.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SMOOTH_BLOOD_FLOW(ModEffectRegistry.SMOOTH_BLOOD_FLOW.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    MENTAL_STIMULATION(ModEffectRegistry.MENTAL_STIMULATION.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    APPETITE_BOOST(ModEffectRegistry.APPETITE_BOOST.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    SUSTAINED_ENERGY(ModEffectRegistry.SUSTAINED_ENERGY.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    STRESS_RELIEF(ModEffectRegistry.STRESS_RELIEF.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get()),
    HYDRATION_AND_PLUMPNESS(ModEffectRegistry.HYDRATION_AND_PLUMPNESS.get(), COMBINATION_BUFF_DURATION.get(), COMBINATION_BUFF_PROBABILITY.get());

    private final MobEffect effect;
    private final double duration;
    private final double probability;

    NutrientBuff(MobEffect effect, double duration, double probability) {
        this.effect = effect;
        this.duration = duration;
        this.probability = probability;
    }

    public MobEffect getEffect() {
        return effect;
    }

    public double getDuration() {
        return duration;
    }

    public double getProbability() {
        return probability;
    }
}
