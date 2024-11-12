package mangopill.customized.client.event.tooltip;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = Dist.CLIENT, modid = Customized.MODID)
public class PropertyValueTooltip {
    @SubscribeEvent
    public static void onTooltip(final ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null || player.level() == null) {
            return;
        }
        @NotNull PropertyValue propertyValue = PropertyValueRecipe.getPropertyValue(event.getItemStack(), player.level());
        if (propertyValue.isEmpty()) {
            return;
        }
        propertyValue.toSet().forEach(entry -> {
            MutableComponent itemComponent = Component.translatable("tooltip." + Customized.MODID + ".property_value",
                    Component.translatable("property." + Customized.MODID + ".nutrient_category." + entry.getKey().name().toLowerCase()),
                    entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().getColor())).append("%");
            event.getToolTip().add(itemComponent);
        });
    }
}
