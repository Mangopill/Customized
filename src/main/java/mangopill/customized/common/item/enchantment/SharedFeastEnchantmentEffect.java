package mangopill.customized.common.item.enchantment;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.CEnchantmentComponentRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Customized.MODID)
public class SharedFeastEnchantmentEffect {
    @SubscribeEvent
    public static void onSharedFeastTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (level.isClientSide) {
            return;
        }
        EnchantmentHelper.runIterationOnItem(helmet, (e, l) -> {
            EnchantmentValueEffect effect = e.value().effects().get(CEnchantmentComponentRegistry.SHARED_FEAST.get());
            if (effect == null) {
                return;
            }
            double range = 5.0D + l * 2.0D;
            List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(range));
            for (Player nearbyPlayer : nearbyPlayers) {
                if (nearbyPlayer.equals(player)) {
                    continue;
                }
                player.getActiveEffects().stream()
                        .filter(i -> i.getEffect().value().isBeneficial())
                        .forEach(effectInstance -> shareEffect(l, nearbyPlayer, effectInstance, level));
            }
        });
    }

    public static void shareEffect(int l, LivingEntity entity, MobEffectInstance effectInstance, Level level) {
        MobEffectInstance shared = new MobEffectInstance(
                effectInstance.getEffect(),
                (int) (effectInstance.getDuration() * Math.min(l * 0.2F, 0.9F)),
                Math.min(effectInstance.getAmplifier(), l - 1),
                effectInstance.isAmbient(),
                effectInstance.isVisible(),
                effectInstance.showIcon()
        );
        entity.addEffect(shared);
        if (level instanceof ServerLevel serverLevel && serverLevel.getRandom().nextInt(5) == 0) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY() + 0.7D, entity.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.05D);
        }
    }
}
