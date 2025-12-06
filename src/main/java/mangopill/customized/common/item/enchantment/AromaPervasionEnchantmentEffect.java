package mangopill.customized.common.item.enchantment;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.CEnchantmentComponentRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
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

@EventBusSubscriber(modid = Customized.MODID)
public class AromaPervasionEnchantmentEffect {
    @SubscribeEvent
    public static void onAromaPervasionTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (level.isClientSide) {
            return;
        }
        EnchantmentHelper.runIterationOnItem(helmet, (e, l) -> {
            EnchantmentValueEffect effect = e.value().effects().get(CEnchantmentComponentRegistry.AROMA_PERVASION.get());
            if (effect == null) {
                return;
            }
            double range = 4.0D + l * 1.0D;
            List<Mob> hostileMobs = level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(range), entity -> entity instanceof Enemy);
            for (Mob mob : hostileMobs) {
                if (level.random.nextFloat() > 0.2F) {
                    continue;
                }
                mob.hurt(level.damageSources().magic(), 0.05F * l);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, mob.getX(), mob.getY() + 0.5D, mob.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.05D);
                }
            }
            List<Animal> animals = level.getEntitiesOfClass(Animal.class, player.getBoundingBox().inflate(range));
            for (Animal animal : animals) {
                if (animal.distanceTo(player) < 3.0F) {
                    continue;
                }
                animal.getLookControl().setLookAt(player);
                animal.getNavigation().moveTo(player, 0.6D + l * 0.1D);
                if (level.random.nextFloat() > 0.1F) {
                    continue;
                }
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART, animal.getX(), animal.getY() + 0.5D, animal.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.05D);
                }
            }
        });
    }
}
