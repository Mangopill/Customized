package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 * This MobEffect can increase the player's damage.
 */
public class AppetiteBoostEffect extends ModMobEffect implements ShrinkNutritionMobEffect, ShrinkSaturationMobEffect ,CombinationMobEffect {

    public AppetiteBoostEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "616B21B1-1B06-448A-8E3F-D6078AB92727", 4.0, AttributeModifier.Operation.ADDITION);
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
