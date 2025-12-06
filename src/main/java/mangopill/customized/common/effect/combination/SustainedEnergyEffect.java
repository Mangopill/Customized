package mangopill.customized.common.effect.combination;

import mangopill.customized.common.effect.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 *This MobEffect can give the player the DAMAGE_RESISTANCE effect.
 */
public class SustainedEnergyEffect extends CMobEffect {

    public SustainedEnergyEffect(int color) {
        super(color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (amplifier + 1) * 600, amplifier));
        }
    }
}
