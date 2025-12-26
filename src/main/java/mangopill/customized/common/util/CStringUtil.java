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

    public static String formatPercent(double value, int decimals) {
        return String.format("%." + decimals + "f%%", value * 100);
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

    public static MutableComponent getItemTextBuffComponent(FoodProperties.PossibleEffect buff, float ticksPerSecond) {
        MobEffectInstance effect = buff.effect();
        MobEffect mobEffect = effect.getEffect().value();
        int i = Mth.floor((float) effect.getDuration());
        Component component = literal(StringUtil.formatTickDuration(i, ticksPerSecond));
        return C_ITEM_TEXT.create("buff", mobEffect.getDisplayName().copy().append(getEnchantmentLevelComponent(effect.getAmplifier())), component).withStyle(mobEffect.getCategory().getTooltipFormatting());
    }

    public static MutableComponent getItemTextLootChanceLevelComponent(float chanceLevel) {
        return C_ITEM_TEXT.create("loot_chance_level", formatPercent(chanceLevel, 2)).withStyle(ChatFormatting.GREEN);
    }
}
