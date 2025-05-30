package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 *This MobEffect grants the player the DAMAGE_RESISTANCE effect upon activation and increases their maximum health.
 */
public class VitalityRestorationEffect extends ModMobEffect implements ShrinkSaturationMobEffect, CombinationMobEffect {

    public VitalityRestorationEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "C60B87C2-E28C-4834-A204-D89066CA7657", 4.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        MobEffectInstance instance = livingEntity.getEffect(this);
        if (livingEntity instanceof ServerPlayer player) {
            if (instance != null && instance.getDuration() == 1) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (amplifier + 1) * 600, amplifier));
            }
        }
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(SOUR, SWEET));
    }
}
