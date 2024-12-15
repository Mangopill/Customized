package mangopill.customized.client.event.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.util.PropertyValueUtil;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;

@EventBusSubscriber(modid = Customized.MODID, value = Dist.CLIENT)
public class PropertyValueTooltip {
    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (player == null || player.level() == null) {
            return;
        }
        @NotNull PropertyValue propertyValue = PropertyValueUtil.getPropertyValue(stack, player.level());
        FoodProperties foodProperty = getFoodPropertyByPropertyValue(player.level(), Collections.singletonList(stack), false);
        if (propertyValue.isEmpty()) {
            return;
        }
        if (!isCtrlKeyPressed()) {
            if (canShow()) {
                event.getToolTip().add(Component.translatable("tooltip." + Customized.MODID + ".is_ctrl_key_pressed")
                        .withStyle(ChatFormatting.DARK_GRAY));
                return;
            }
        }
        if (SHOW_NUTRIENT_VALUE_TOOLTIP.get()) {
            propertyValue.toSet().forEach(entry -> {
                MutableComponent propertyComponent = Component.translatable("tooltip." + Customized.MODID + ".property_value",
                        Component.translatable("property." + Customized.MODID + ".nutrient_category." + entry.getKey().getSerializedName()),
                        entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().getColor())).append("%");
                event.getToolTip().add(propertyComponent);
            });
        }
        if (foodProperty.equals(FoodValue.NULL)) {
            return;
        }
        if (SHOW_ESTIMATED_VALUE_TOOLTIP.get()) {
            MutableComponent estimatedComponent = Component.translatable("tooltip." + Customized.MODID + ".estimated_value",
                    Component.translatable("estimated." + Customized.MODID + ".nutritional_value"),
                    foodProperty.nutrition(), foodProperty.saturation()).withStyle(ChatFormatting.GREEN);
            event.getToolTip().add(estimatedComponent);
        }
        if (SHOW_ESTIMATED_BUFF_TOOLTIP.get()) {
            if (!foodProperty.effects().isEmpty()) {
                foodProperty.effects().forEach(buff -> {
                    MutableComponent estimatedBuff = Component.translatable("tooltip." + Customized.MODID + ".estimated_buff",
                            Component.translatable("estimated." + Customized.MODID + ".buff"),
                            Component.translatable(buff.effectSupplier().get().getEffect().value().getDescriptionId()),
                            buff.effectSupplier().get().getDuration()).withStyle(ChatFormatting.GREEN);
                    event.getToolTip().add(estimatedBuff);
                });
            }
        }
    }

    private static boolean canShow() {
        return SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_ESTIMATED_BUFF_TOOLTIP.get();
    }

    private static boolean isCtrlKeyPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_RCONTROL);
    }
}
