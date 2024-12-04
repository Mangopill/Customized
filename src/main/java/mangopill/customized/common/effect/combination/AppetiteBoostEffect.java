package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

public class AppetiteBoostEffect extends ModMobEffect implements ShrinkNutritionMobEffect, ShrinkSaturationMobEffect ,CombinationMobEffect {
    public AppetiteBoostEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.strength"), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }
    @Override
    public float getShrinkNutritionModifier() {
        return 0.2F;
    }

    @Override
    public float getShrinkSaturationModifier() {
        return 0.2F;
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(SOUR, SPICY));
    }
}
