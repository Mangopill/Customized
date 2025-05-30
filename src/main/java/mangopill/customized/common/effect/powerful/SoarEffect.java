package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

/**
 *This MobEffect allows the player to stay flying, but prevents them from landing.
 */
public class SoarEffect extends ModMobEffect {

    public SoarEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "32879BDD-F20F-42DD-AEBD-BC9B8EE00329", 0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        }
    }
}
