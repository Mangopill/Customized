package mangopill.customized.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

@OnlyIn(Dist.CLIENT)
public final class TooltipUtil {
    private TooltipUtil() {
    }

    public static void propertyValueTooltip(List<Component> components, ItemStack stack, Level level) {
        PropertyValue propertyValue = getPropertyValue(stack, level);
        FoodProperties foodProperty = getFoodPropertyByPropertyValue(level, List.of(stack), false);
        if (propertyValue.isEmpty()) return;
        if (!isCtrlKeyPressed() && canShow()) {
            components.add(getComponent("tooltip." + Customized.MODID + ".is_ctrl_key_pressed")
                    .withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        if (SHOW_NUTRIENT_VALUE_TOOLTIP.get()) {
            addFoodCategory(components, stack);
            propertyValue.getValue().forEach((key, value) -> {
                List<NutrientCategoryRecipe> recipes = getNutrientCategoryByName(level, key);
                if (!recipes.isEmpty() && recipes.getFirst() != null) {
                    Component propertyComponent =
                            getComponent("tooltip." + Customized.MODID + ".property_value",
                                    getComponent("property." + Customized.MODID + ".nutrient_category." + key), value)
                                    .withColor(recipes.getFirst().getColorWithAlpha()).append("%");
                    components.add(propertyComponent);
                }
            });
        }
        if (foodProperty.equals(FoodValue.EMPTY)) return;
        if (SHOW_ESTIMATED_VALUE_TOOLTIP.get()) {
            MutableComponent estimatedComponent = getComponent("tooltip." + Customized.MODID + ".estimated_value",
                    getComponent("estimated." + Customized.MODID + ".nutritional_value"),
                    foodProperty.nutrition(), foodProperty.saturation()).withStyle(ChatFormatting.GREEN);
            components.add(estimatedComponent);
        }
        if (SHOW_ESTIMATED_BUFF_TOOLTIP.get()) {
            if (!foodProperty.effects().isEmpty()) {
                foodProperty.effects().forEach(buff -> {
                    MutableComponent estimatedBuff = getComponent("tooltip." + Customized.MODID + ".estimated_buff",
                            getComponent("estimated." + Customized.MODID + ".buff"),
                            getComponent(buff.effectSupplier().get().getEffect().value().getDescriptionId()),
                            buff.effectSupplier().get().getDuration()).withStyle(ChatFormatting.GREEN);
                    components.add(estimatedBuff);
                });
            }
        }
    }

    public static void addFoodCategory(List<Component> components, ItemStack stack) {
        components.add(getComponent("tooltip." + Customized.MODID + (stack.is(ModTag.SEASONING) ? ".food_category_seasoning" : ".food_category_food")).withStyle(ChatFormatting.BLUE));
    }

    public static boolean canShow() {
        return SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_ESTIMATED_BUFF_TOOLTIP.get();
    }

    public static boolean isCtrlKeyPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_RCONTROL);
    }
}
