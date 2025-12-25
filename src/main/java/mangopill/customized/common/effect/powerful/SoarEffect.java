package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
/**
 *This MobEffect allows the player to stay flying, but prevents them from landing.
 */
public class SoarEffect extends CMobEffect {

    public SoarEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.soar_speed"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        }
        return true;
    }
}
