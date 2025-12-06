package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * This MobEffect can increase the player's damage.
 */
public class AppetiteBoostEffect extends CMobEffect {

    public AppetiteBoostEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.appetite_boost_strength"), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
