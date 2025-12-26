package mangopill.customized.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.*;

import java.util.List;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;

@OnlyIn(Dist.CLIENT)
public final class TooltipUtil {
    private TooltipUtil() {}

    public static void propertyValueTooltip(List<Component> components, ItemStack stack, Level level) {
        PropertyValue propertyValue = getPropertyValue(stack, level);
        FoodProperties foodProperty = getFoodPropertyByPropertyValue(level, List.of(stack), false);
        if (propertyValue.isEmpty()) return;
        if (!isCtrlKeyPressed() && canShow()) {
            components.add(C_TOOLTIP.create("is_ctrl_key_pressed").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        if (SHOW_NUTRIENT_VALUE_TOOLTIP.get()) {
            components.add(C_TOOLTIP.create(stack.is(ModTag.SEASONING) ? "food_category_seasoning" : "food_category_food").withStyle(ChatFormatting.BLUE));
            propertyValue.getValue().forEach((key, value) -> {
                List<NutrientCategoryRecipe> recipes = getNutrientCategoryByName(level, key);
                if (!recipes.isEmpty() && recipes.getFirst() != null) {
                    components.add(getTooltipPropertyComponent(key, value, recipes.getFirst().getColorWithAlpha()));
                }
            });
        }
        if (foodProperty.equals(FoodValue.EMPTY)) return;
        if (SHOW_ESTIMATED_VALUE_TOOLTIP.get()) {
            components.add(C_TOOLTIP.create("estimated_value", C_TOOLTIP.create("estimated_value_title"), foodProperty.nutrition(), foodProperty.saturation()).withStyle(ChatFormatting.GREEN));
        }
        if (SHOW_ESTIMATED_BUFF_TOOLTIP.get() && !foodProperty.effects().isEmpty()) {
            foodProperty.effects().forEach(effect -> components.add(C_TOOLTIP.create("estimated_buff",
                    C_TOOLTIP.create("estimated_buff_title"), effect.effectSupplier().get().getEffect().value().getDisplayName(),
                    effect.effectSupplier().get().getDuration()).withStyle(ChatFormatting.GREEN)));
        }
    }

    public static boolean canShow() {
        return SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_NUTRIENT_VALUE_TOOLTIP.get() || SHOW_ESTIMATED_BUFF_TOOLTIP.get();
    }

    public static boolean isCtrlKeyPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_RCONTROL);
    }
}
