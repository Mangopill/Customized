package mangopill.customized.common.effect;

import mangopill.customized.common.util.category.NutrientCategory;

import java.util.*;

public interface CombinationMobEffect {
    /**
     *Set the nutrient values that must be met in order to grant the buff.
     * @return List:Set<NutrientCategory>
     */
    List<Set<NutrientCategory>> getCategorySet();
}
