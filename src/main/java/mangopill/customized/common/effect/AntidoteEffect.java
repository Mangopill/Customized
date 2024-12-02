package mangopill.customized.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AntidoteEffect extends MobEffect {
    public AntidoteEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
}
