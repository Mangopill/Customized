package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static mangopill.customized.common.util.category.NutrientCategory.*;

/**
 * This effect prevents Phantoms from spawning near the player
 * and grants the Night Vision effect.
 */
public class NoctenergySurgeEffect extends ModMobEffect implements CombinationMobEffect {

    public NoctenergySurgeEffect(int color) {
        super(color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        MobEffectInstance instance = livingEntity.getEffect(this);
        if (livingEntity instanceof ServerPlayer player) {
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            if (instance != null && instance.getDuration() == 1) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, (amplifier + 1) * 1800, amplifier));
            }
        }
    }

    @Override
    public List<Set<NutrientCategory>> getCategorySet() {
        return List.of(Set.of(WATER, PROTEIN), Set.of(WATER, DIETARY_FIBER));
    }
}
