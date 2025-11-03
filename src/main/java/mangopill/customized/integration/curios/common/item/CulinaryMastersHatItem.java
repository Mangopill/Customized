package mangopill.customized.integration.curios.common.item;

import mangopill.customized.Customized;
import mangopill.customized.common.util.value.NutrientBuff;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

import static mangopill.customized.common.CustomizedConfig.*;

public class CulinaryMastersHatItem implements ICurioItem {
    private static final int TICK_INTERVAL = 5000;
    private static final int RANDOM_DURATION = 10000;
    private static final int RANDOM_AMPLIFIER = 2;
    private static final int FOOD_LEVEL = 1;
    private static final float SATURATION_LEVEL = 0.1F;
    private static final int SATIETY_RENEWAL_RANDOM = 850;
    private static final int SATIETY_RENEWAL_FIX = 10;
    private int waitTick = 0;

    public CulinaryMastersHatItem() {
        super();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && !player.level().isClientSide) {
            hungerProtection(player);
            mealsBlessing(player);
            satietyRenewal(player);
        }
    }

    private void hungerProtection(Player player) {
        if (player.getFoodData().getFoodLevel() > 6){
            return;
        }
        player.getFoodData().eat(FOOD_LEVEL, SATURATION_LEVEL);
    }

    private void mealsBlessing(Player player) {
        RandomSource random = player.getRandom();
        waitTick++;
        if (waitTick < TICK_INTERVAL) {
            return;
        }
        Set<NutrientBuff> allBuffs = EnumSet.allOf(NutrientBuff.class);
        List<NutrientBuff> buffList = new ArrayList<>(allBuffs);
        if (buffList.isEmpty()) {
            return;
        }
        NutrientBuff selectedBuff = buffList.get(random.nextInt(buffList.size()));
        player.addEffect(new MobEffectInstance(selectedBuff.getEffect(), random.nextInt(RANDOM_DURATION), random.nextInt(RANDOM_AMPLIFIER)));
        waitTick = 0;
        if (!CULINARY_MASTERS_HAT_MESSAGE.get()) {
            return;
        }
        player.displayClientMessage(Component.translatable("curios.message.customized.culinary_masters_hat.meals_blessing",
                Component.translatable(selectedBuff.getEffect().value().getDescriptionId())), true);
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 0.5D, player.getZ(), 10, 0.0D, 0.0D, 0.0D, 0.05D);
        }
    }

    private void satietyRenewal(Player player) {
        if (player.getFoodData().needsFood()) {
            return;
        }
        RandomSource random = player.getRandom();
        if (random.nextInt(SATIETY_RENEWAL_RANDOM) > 1) {
            return;
        }
        List<ItemStack> repairableItems = new ArrayList<>();
        player.getAllSlots().forEach(repairableItems::add);
        repairableItems.addAll(player.getInventory().items);
        List<ItemStack> damagedItems = repairableItems.stream()
                .filter(item -> !item.isEmpty())
                .filter(ItemStack::isDamaged)
                .filter(IItemStackExtension::isRepairable)
                .toList();
        if (damagedItems.isEmpty()) {
            return;
        }
        ItemStack itemToRepair = damagedItems.get(random.nextInt(damagedItems.size()));
        int newDamage = Math.max(0, itemToRepair.getDamageValue() - random.nextInt(SATIETY_RENEWAL_FIX));
        itemToRepair.setDamageValue(newDamage);
        if (!CULINARY_MASTERS_HAT_MESSAGE.get()) {
            return;
        }
        player.displayClientMessage(Component.translatable("curios.message.customized.culinary_masters_hat.satiety_renewal",
                Component.empty().append(itemToRepair.getDisplayName())), true);
        player.level().playSound(null, player, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.6F, 0.6F);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, Item.TooltipContext context, ItemStack stack) {
        List<Component> newTooltips = new ArrayList<>(tooltips);
        newTooltips.add(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.hunger_protection_title").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.hunger_protection_text").withStyle(ChatFormatting.YELLOW)));
        newTooltips.add(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.meals_blessing_title").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.meals_blessing_text").withStyle(ChatFormatting.YELLOW)));
        newTooltips.add(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.satiety_renewal_title").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("curios.tooltip." + Customized.MODID + ".culinary_masters_hat.satiety_renewal_text").withStyle(ChatFormatting.YELLOW)));
        return newTooltips;
    }
}
