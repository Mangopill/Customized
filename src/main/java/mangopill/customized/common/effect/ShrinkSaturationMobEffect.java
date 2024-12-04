package mangopill.customized.common.effect;

public interface ShrinkSaturationMobEffect {
    /**
     * Set the ratio at which saturation is reduced.
     * @return The reduction ratio.
     */
    default float getShrinkSaturationModifier() {
        return 0.1F;
    };
}
