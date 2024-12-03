package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class MetabolismEffect extends ModMobEffect {
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
}
