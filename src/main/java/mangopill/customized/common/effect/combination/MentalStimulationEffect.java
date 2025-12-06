package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * This MobEffect can make the player luckier.
 */
public class MentalStimulationEffect extends CMobEffect {

    public MentalStimulationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.LUCK, ResourceLocation.withDefaultNamespace("effect.mental_stimulation_luck"), 0.6, AttributeModifier.Operation.ADD_VALUE);
    }
}
