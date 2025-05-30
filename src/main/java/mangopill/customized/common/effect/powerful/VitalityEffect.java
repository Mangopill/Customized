package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;

import javax.annotation.Nonnull;

/**
 *This MobEffect can increase the player's health regeneration speed, as well as MOVEMENT_SPEED, ATTACK_SPEED, MAX_HEALTH, and ATTACK_DAMAGE.
 */
public class VitalityEffect extends ModMobEffect {

    public VitalityEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "CAF76750-E6FC-4E4D-A0B3-3C4CBA7305C2", 0.6F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, "B1FD5826-2F8B-4FE9-891A-40677EE29810", 0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "AC4A7E6E-D315-4BDA-B07A-47AD98AF9148", 12.0, AttributeModifier.Operation.ADDITION);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "D73B0FAE-538C-4592-87C8-1EB0207127DB", 9.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % (40 / (amplifier + 1)) == 0;
    }
}
