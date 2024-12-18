package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 *This MobEffect grants the player the DAMAGE_RESISTANCE effect upon activation and increases their maximum health.
 */
public class VitalityRestorationEffect extends ModMobEffect implements ShrinkSaturationMobEffect, CombinationMobEffect {

    public VitalityRestorationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.health_boost"), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, amplifier));
        }
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(SOUR, SWEET));
    }
}
