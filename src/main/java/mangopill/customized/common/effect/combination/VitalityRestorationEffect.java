package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 *This MobEffect grants the player the DAMAGE_RESISTANCE effect upon activation and increases their maximum health.
 */
public class VitalityRestorationEffect extends CMobEffect {

    public VitalityRestorationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.vitality_restoration_health_boost"), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (amplifier + 1) * 600, amplifier));
        }
    }
}
