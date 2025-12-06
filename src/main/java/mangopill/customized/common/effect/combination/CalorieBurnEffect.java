package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 *This MobEffect can increase the player's attack speed, movement speed, and mining speed.
 */
public class CalorieBurnEffect extends CMobEffect {

    public CalorieBurnEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.calorie_burn_speed"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.calorie_burn_haste"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
