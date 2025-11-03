package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
/**
 *This MobEffect can increase the player's health regeneration speed, as well as MOVEMENT_SPEED, ATTACK_SPEED, MAX_HEALTH, and ATTACK_DAMAGE.
 */
public class VitalityEffect extends CMobEffect {

    public VitalityEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.vitality_speed"), 0.6F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.vitality_haste"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.vitality_health_boost"), 12.0, AttributeModifier.Operation.ADD_VALUE);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.vitality_strength"), 9.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (40 / Math.min(40 , amplifier + 1)) == 0;
    }
}
