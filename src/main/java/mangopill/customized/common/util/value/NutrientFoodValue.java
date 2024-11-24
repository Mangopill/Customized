package mangopill.customized.common.util.value;

import static mangopill.customized.common.CustomizedConfig.*;

public enum NutrientFoodValue {
    WATER(WATER_NUTRITION_VALUE.get(), WATER_SATURATION_VALUE.get()),
    PROTEIN(PROTEIN_NUTRITION_VALUE.get(), PROTEIN_SATURATION_VALUE.get()),
    LIPID(LIPID_NUTRITION_VALUE.get(), LIPID_SATURATION_VALUE.get()),
    CARBOHYDRATE(CARBOHYDRATE_NUTRITION_VALUE.get(), CARBOHYDRATE_SATURATION_VALUE.get()),
    VITAMIN(VITAMIN_NUTRITION_VALUE.get(), VITAMIN_SATURATION_VALUE.get()),
    MINERAL(MINERAL_NUTRITION_VALUE.get(), MINERAL_SATURATION_VALUE.get()),
    DIETARY_FIBER(DIETARY_FIBER_NUTRITION_VALUE.get(), DIETARY_FIBER_SATURATION_VALUE.get()),

    COLD(COLD_NUTRITION_VALUE.get(), COLD_SATURATION_VALUE.get()),
    WARM(WARM_NUTRITION_VALUE.get(), WARM_SATURATION_VALUE.get()),

    ECOLOGY(ECOLOGY_NUTRITION_VALUE.get(), ECOLOGY_SATURATION_VALUE.get()),
    DREAD(DREAD_NUTRITION_VALUE.get(), DREAD_SATURATION_VALUE.get()),
    NOTHINGNESS(NOTHINGNESS_NUTRITION_VALUE.get(), NOTHINGNESS_SATURATION_VALUE.get()),

    SOUR(SOUR_NUTRITION_VALUE.get(), SOUR_SATURATION_VALUE.get()),
    SWEET(SWEET_NUTRITION_VALUE.get(), SWEET_SATURATION_VALUE.get()),
    BITTER(BITTER_NUTRITION_VALUE.get(), BITTER_SATURATION_VALUE.get()),
    SPICY(SPICY_NUTRITION_VALUE.get(), SPICY_SATURATION_VALUE.get()),
    SALTY(SALTY_NUTRITION_VALUE.get(), SALTY_SATURATION_VALUE.get()),
    NUMBING(NUMBING_NUTRITION_VALUE.get(), NUMBING_SATURATION_VALUE.get());

    private final double nutrition;
    private final double saturation;

    NutrientFoodValue(double nutrition, double saturation) {
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    public double getNutrition() {
        return nutrition;
    }

    public double getSaturation() {
        return saturation;
    }
}
