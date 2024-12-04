package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.CombinationMobEffect;
import mangopill.customized.common.effect.ModMobEffect;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 *This MobEffect can increase the player's health regeneration speed, but it will slightly accelerate food consumption.
 */
public class MetabolismEffect extends ModMobEffect implements CombinationMobEffect {

    public MetabolismEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            player.causeFoodExhaustion(0.002F * (float)(amplifier + 1));
        }
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (80 / (amplifier + 1)) == 0;
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(MINERAL, VITAMIN, DIETARY_FIBER));
    }
}
