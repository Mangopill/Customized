package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.CombinationMobEffect;
import mangopill.customized.common.effect.ModMobEffect;
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
 *This MobEffect can increase MAX_HEALTH, ATTACK_DAMAGE, and grant DAMAGE_RESISTANCE.
 */
public class RobustEffect extends ModMobEffect implements CombinationMobEffect {

    public RobustEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "78E372B4-08F4-4A18-AA75-9809411743B5", 5.0, AttributeModifier.Operation.ADDITION);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "94F1AC48-A379-4F4F-81F9-A3589D8A2A46", 4.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        MobEffectInstance instance = livingEntity.getEffect(this);
        if (livingEntity instanceof ServerPlayer player) {
            if (instance != null && instance.getDuration() == 1) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (amplifier + 1) * 500, amplifier));
            }
        }
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(PROTEIN, LIPID, CARBOHYDRATE));
    }
}
