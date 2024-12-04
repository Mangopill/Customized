package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

public class MentalStimulationEffect extends ModMobEffect implements ShrinkNutritionMobEffect, CombinationMobEffect {
    public MentalStimulationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.LUCK, ResourceLocation.withDefaultNamespace("effect.luck"), 1.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(FRESH, FRAGRANT));
    }
}
