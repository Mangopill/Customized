package mangopill.customized.common.effect;

public interface ShrinkNutritionMobEffect {
    /**
     * Set the ratio at which nutrition is reduced.
     * @return The reduction ratio.
     */
    default float getShrinkNutritionModifier() {
        return 0.1F;
    }
}
