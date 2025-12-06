package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 *This MobEffect can increase the player's health regeneration speed, but it will slightly accelerate food consumption.
 */
public class MetabolismEffect extends CMobEffect {

    public MetabolismEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
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
        return duration % (80 / Math.min(80 , amplifier + 1)) == 0;
    }
}
