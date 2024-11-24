package mangopill.customized.client.event.tooltip;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;

@EventBusSubscriber(value = Dist.CLIENT, modid = Customized.MODID)
public class PropertyValueTooltip {
    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null || player.level() == null) {
            return;
        }
        if(SHOW_NUTRIENT_VALUE_TOOLTIP.get()){
            @NotNull PropertyValue propertyValue = PropertyValueRecipe.getPropertyValue(event.getItemStack(), player.level());
            if (!propertyValue.isEmpty()) {
                propertyValue.toSet().forEach(entry -> {
                    MutableComponent propertyComponent = Component.translatable("tooltip." + Customized.MODID + ".property_value",
                            Component.translatable("property." + Customized.MODID + ".nutrient_category." + entry.getKey().name().toLowerCase()),
                            entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().getColor())).append("%");
                    event.getToolTip().add(propertyComponent);
                });
            }
        }
        FoodProperties foodProperty = getFoodPropertyByPropertyValue(player.level(), Collections.singletonList(event.getItemStack()), false);
        if (foodProperty.equals(FoodValue.NULL)) {
            return;
        }
        if (SHOW_ESTIMATED_VALUE_TOOLTIP.get()){
            MutableComponent estimatedComponent = Component.translatable("tooltip." + Customized.MODID + ".estimated_value",
                    Component.translatable("estimated." + Customized.MODID + ".nutritional_value"),
                    foodProperty.nutrition(), foodProperty.saturation()).withStyle(Style.EMPTY.withColor(0x00A800));
            event.getToolTip().add(estimatedComponent);
        }
        if (SHOW_ESTIMATED_BUFF_TOOLTIP.get()){
            if (!foodProperty.effects().isEmpty()){
                foodProperty.effects().forEach(buff -> {
                    MutableComponent estimatedBuff = Component.translatable("tooltip." + Customized.MODID + ".estimated_buff",
                            Component.translatable("estimated." + Customized.MODID + ".buff"),
                            Component.translatable(buff.effectSupplier().get().getEffect().value().getDescriptionId()),
                            buff.effectSupplier().get().getDuration()).withStyle(Style.EMPTY.withColor(0x00A800));
                    event.getToolTip().add(estimatedBuff);
                });
            }
        }
    }
}
