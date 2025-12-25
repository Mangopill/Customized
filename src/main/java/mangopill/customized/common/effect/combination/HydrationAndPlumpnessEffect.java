package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.CMobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;

/**
 *This MobEffect can give the player the HEAL effect. On top of that, it will also continuously grant the WATER_BREATHING effect.
 */
public class HydrationAndPlumpnessEffect extends CMobEffect {

    public HydrationAndPlumpnessEffect(int color) {
        super(color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, (amplifier + 1) * 260, amplifier));
        }
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.HEAL, (amplifier + 1) * 150, amplifier));
        }
    }
}
