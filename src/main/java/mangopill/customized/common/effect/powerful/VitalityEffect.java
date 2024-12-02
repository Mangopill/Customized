package mangopill.customized.common.effect.powerful;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

public class VitalityEffect extends MobEffect {
    public VitalityEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.speed"), 0.6F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.haste"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        super.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.health_boost"), 12.0, AttributeModifier.Operation.ADD_VALUE);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.strength"), 9.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide) {
            livingEntity.heal(1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (40 / (amplifier + 1)) == 0;
    }
}
