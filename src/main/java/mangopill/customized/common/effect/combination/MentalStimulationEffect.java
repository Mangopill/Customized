package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 * This MobEffect can make the player luckier.
 */
public class MentalStimulationEffect extends ModMobEffect implements ShrinkNutritionMobEffect, CombinationMobEffect {

    public MentalStimulationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.LUCK, "46FA7F08-FCA9-4ED7-9410-2ED7920C9C6B", 0.6, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(FRESH, FRAGRANT));
    }
}
