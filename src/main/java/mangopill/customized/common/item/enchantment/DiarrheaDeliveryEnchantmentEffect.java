package mangopill.customized.common.item.enchantment;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.CEnchantmentComponentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.stream.Stream;

import static mangopill.customized.common.item.enchantment.SharedFeastEnchantmentEffect.shareEffect;

@EventBusSubscriber(modid = Customized.MODID)
public class DiarrheaDeliveryEnchantmentEffect {
    @SubscribeEvent
    public static void onSharedFeastTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (level.isClientSide) {
            return;
        }
        EnchantmentHelper.runIterationOnItem(helmet, (e, l) -> {
            EnchantmentValueEffect effect = e.value().effects().get(CEnchantmentComponentRegistry.DIARRHEA_DELIVERY.get());
            if (effect == null) {
                return;
            }
            double range = 5.0D + l * 2.0D;
            List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class,
                    player.getBoundingBox().inflate(range));
            List<Mob> hostileMobs = level.getEntitiesOfClass(Mob.class,
                    player.getBoundingBox().inflate(range),
                    entity -> entity instanceof Enemy);
            Stream.concat(nearbyPlayers.stream(), hostileMobs.stream())
                    .forEach(entity -> player.getActiveEffects().stream()
                            .filter(instance -> !instance.getEffect().value().isBeneficial() && !entity.equals(player))
                            .forEach(effectInstance -> shareEffect(l, entity, effectInstance, level)));
        });
    }
}
