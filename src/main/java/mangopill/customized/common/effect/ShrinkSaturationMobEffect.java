package mangopill.customized.common.effect;

import static mangopill.customized.common.CustomizedConfig.*;

public interface ShrinkSaturationMobEffect {
    /**
     * Set the ratio at which saturation is reduced.
     * @return The reduction ratio.
     */
    default float getShrinkSaturationModifier() {
        return SHRINK_SATURATION.get().floatValue();
    }
}
