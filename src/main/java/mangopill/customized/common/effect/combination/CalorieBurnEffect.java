package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 *This MobEffect can increase the player's attack speed, movement speed, and mining speed.
 */
public class CalorieBurnEffect extends ModMobEffect implements ShrinkNutritionMobEffect, CombinationMobEffect {

    public CalorieBurnEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.speed"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.haste"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(BITTER, SPICY));
    }
}
