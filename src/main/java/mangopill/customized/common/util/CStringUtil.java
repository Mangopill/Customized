package mangopill.customized.common.util;

import mangopill.customized.Customized;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.food.FoodProperties;

public final class CStringUtil {
    private CStringUtil() {}

    public static ResourceLocation getCLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Customized.MODID, path);
    }

    public static ResourceLocation getCPngLoc(String path) {
        return getCLoc(path + ".png");
    }

    public static TextColor getColorFromString(String color) {
        return TextColor.fromRgb(getColorWithAlphaFromString(color));
    }

    public static int getColorWithAlphaFromString(String color) {
        return Long.decode(color).intValue();
    }

    /**
     * Formats a decimal value as a percentage string.
     * <p>
     * This method converts a decimal value (e.g., 0.75) to a formatted percentage
     * string (e.g., "75.00%") with the specified number of decimal places.
     * @param value The decimal value to format (0.0 to 1.0)
     * @param decimals The number of decimal places to display
     * @return A formatted percentage string
     */
    public static String formatPercent(double value, int decimals) {
        return String.format("%." + decimals + "f%%", value * 100);
    }

    /**
     * Converts Minecraft tick time to a human-readable time component.
     * <p>
     * Minecraft's day-night cycle uses 24000 ticks per day. This method converts
     * a tick value to a time string, supporting both 12-hour and 24-hour formats.
     * @param tick The Minecraft tick value (0-23999)
     * @param use24HourFormat If true, uses 24-hour format (00:00-23:59);
     *                        if false, uses 12-hour format with am/pm suffix
     * @return A MutableComponent representing the formatted time
     */
    public static MutableComponent getSimpleTimeFromTick(long tick, boolean use24HourFormat) {
        int timeOfDay = (int)(tick % 24000);
        int hours = timeOfDay >= 18000 ? (timeOfDay - 18000) / 1000 : (6000 + timeOfDay) / 1000;
        int minutes = (int)((timeOfDay % 1000) * 60.0 / 1000.0);
        if (!use24HourFormat) {
            String suffix = hours >= 12 ? "pm" : "am";
            if (hours > 12) {
                hours -= 12;
            } else if (hours == 0) {
                hours = 12;
            }
            return C_TOOLTIP.create(suffix, hours, minutes);
        }
        return literal(String.format("%02d:%02d", hours, minutes));
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translate(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(key, args);
    }

    /**
     * Functional interface for creating components with a suffix.
     * <p>
     * This interface defines a factory method for creating translatable text
     * components with a specific suffix pattern.
     */
    @FunctionalInterface
    public interface ComponentFactory {
        MutableComponent create(String suffix, Object... args);
    }

    public static final ComponentFactory C_TOOLTIP = (s, a) -> translate("tooltip" + "." + Customized.MODID + "." + s, a);
    public static final ComponentFactory C_PROPERTY = (s, a) -> translate("property" + "." + Customized.MODID + "." + s, a);
    public static final ComponentFactory C_ITEM_TEXT = (s, a) -> translate("item_text" + "." + Customized.MODID + "." + s, a);
    public static final ComponentFactory C_MESSAGE = (s, a) -> translate("message" + "." + Customized.MODID + "." + s, a);

    public static MutableComponent getEnchantmentLevelComponent(int level) {
        return translate("enchantment.level" + "." + (level + 1));
    }

    public static MutableComponent getTooltipPropertyComponent(String nutrient, float value, int color) {
        return C_TOOLTIP.create("property_value", getPropertyNutrientCategoryComponent(nutrient), value).append("%").withColor(color);
    }

    public static MutableComponent getPropertyNutrientCategoryComponent(String nutrient) {
        return C_PROPERTY.create("nutrient_category" + "." + nutrient);
    }

    /**
     * Creates a component describing a food buff effect.
     * <p>
     * Formats a food effect with its duration and amplifier into a readable
     * description for item tooltips.
     * @param buff The food effect to describe
     * @param ticksPerSecond The number of ticks per second (for duration conversion)
     * @return A MutableComponent describing the buff effect with proper formatting
     */
    public static MutableComponent getItemTextBuffComponent(FoodProperties.PossibleEffect buff, float ticksPerSecond) {
        MobEffectInstance effect = buff.effect();
        MobEffect mobEffect = effect.getEffect().value();
        int i = Mth.floor((float) effect.getDuration());
        Component component = literal(StringUtil.formatTickDuration(i, ticksPerSecond));
        return C_ITEM_TEXT.create("buff", mobEffect.getDisplayName().copy().append(getEnchantmentLevelComponent(effect.getAmplifier())), component).withStyle(mobEffect.getCategory().getTooltipFormatting());
    }

    /**
     * Creates a component displaying loot chance level as a percentage.
     * <p>
     * Formats a chance value (0.0 to 1.0) as a green-colored percentage string
     * for display in item tooltips.
     * @param chanceLevel The chance level (0.0 to 1.0)
     * @return A MutableComponent with the formatted percentage in green text
     */
    public static MutableComponent getItemTextLootChanceLevelComponent(float chanceLevel) {
        return C_ITEM_TEXT.create("loot_chance_level", formatPercent(chanceLevel, 2)).withStyle(ChatFormatting.GREEN);
    }
}
