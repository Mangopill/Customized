package mangopill.customized.common.util.value;

import mangopill.customized.common.registry.ModEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import static mangopill.customized.common.CustomizedConfig.*;

public enum NutrientBuff {
    ICED(ModEffectRegistry.ICED, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get()),
    WARM_STOMACH(ModEffectRegistry.WARM_STOMACH, NORMAL_BUFF_DURATION.get(), NORMAL_BUFF_PROBABILITY.get());

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
