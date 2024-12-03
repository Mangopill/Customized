package mangopill.customized.common.effect.powerful;

import mangopill.customized.common.effect.ModMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class SoarEffect extends ModMobEffect {
    /**
     *This MobEffect allows the player to stay flying, but prevents them from landing.
     */
    public SoarEffect(int color) {
        super(color);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.speed"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        }
        return true;
    }
}
