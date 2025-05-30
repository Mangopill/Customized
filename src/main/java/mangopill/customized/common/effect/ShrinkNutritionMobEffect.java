package mangopill.customized.common.effect;

import static mangopill.customized.common.CustomizedConfig.*;

public interface ShrinkNutritionMobEffect {
    /**
     * Set the ratio at which nutrition is reduced.
     * @return The reduction ratio.
     */
    default float getShrinkNutritionModifier() {
        return SHRINK_NUTRITION.get().floatValue();
    }
}
