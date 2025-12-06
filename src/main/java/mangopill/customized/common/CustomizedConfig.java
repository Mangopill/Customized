package mangopill.customized.common;

import mangopill.customized.Customized;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CustomizedConfig {
    public static final String MOD_ID = Customized.MODID;
    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec CLIENT_CONFIG;

    public static final String NUTRIENT = "nutrient";
    public static final String BUFF = "buff";
    public static final ModConfigSpec.DoubleValue BUFF_AMPLIFIER;
    public static final String POT = "pot";
    public static final ModConfigSpec.BooleanValue CUSTOM_COOKING;
    public static final ModConfigSpec.BooleanValue RECIPE_COOKING;
    public static final String ENCHANTMENT = "enchantment";
    public static final ModConfigSpec.BooleanValue AURA_OF_CULINARY_ARTS_MESSAGE;
    public static final String INTEGRATION = "integration";
    public static final String CURIOS = "curios";
    public static final String CULINARY_MASTERS_HAT = "culinary_masters_hat";
    public static final ModConfigSpec.BooleanValue CULINARY_MASTERS_HAT_MESSAGE;

    public static final String TOOLTIP = "tooltip";
    public static final ModConfigSpec.BooleanValue SHOW_NUTRIENT_VALUE_TOOLTIP;
    public static final ModConfigSpec.BooleanValue SHOW_ESTIMATED_VALUE_TOOLTIP;
    public static final ModConfigSpec.BooleanValue SHOW_ESTIMATED_BUFF_TOOLTIP;
    public static final String SOUND = "sound";
    public static final ModConfigSpec.BooleanValue CASSEROLE_SOUND;
    public static final ModConfigSpec.BooleanValue ROASTER_SOUND;
    public static final String OVERLAY = "overlay";
    public static final ModConfigSpec.BooleanValue POT_OVERLAY;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();

        commonBuilder.comment(NUTRIENT).push(NUTRIENT);

        commonBuilder.comment(BUFF).push(BUFF);
        BUFF_AMPLIFIER = commonBuilder
                .comment("This value corresponds to how much nutrient value can increase one level of buff.")
                .gameRestart()
                .translation(MOD_ID + ".config.buff_amplifier")
                .defineInRange("buffAmplifier", 3500D, Double.MIN_VALUE, Double.MAX_VALUE);
        commonBuilder.pop();

        commonBuilder.pop();

        commonBuilder.comment(POT).push(POT);
        CUSTOM_COOKING = commonBuilder
                .comment("This boolean value corresponds to whether custom cooking is enabled.")
                .worldRestart()
                .translation(MOD_ID + ".config.custom_cooking")
                .define("enabledCustomCooking", true);
        RECIPE_COOKING = commonBuilder
                .comment("This boolean value corresponds to whether recipe cooking is enabled.")
                .worldRestart()
                .translation(MOD_ID + ".config.recipe_cooking")
                .define("enabledRecipeCooking", true);
        commonBuilder.pop();

        commonBuilder.comment(ENCHANTMENT).push(ENCHANTMENT);
        AURA_OF_CULINARY_ARTS_MESSAGE = commonBuilder
                .comment("This boolean value corresponds to whether aura of culinary arts message is enabled.")
                .worldRestart()
                .translation(MOD_ID + ".config.aura_of_culinary_arts_message")
                .define("enabledAuraOfCulinaryArtsMessage", true);
        commonBuilder.pop();

        commonBuilder.comment(INTEGRATION).push(INTEGRATION);

        commonBuilder.comment(CURIOS).push(CURIOS);

        commonBuilder.comment(CULINARY_MASTERS_HAT).push(CULINARY_MASTERS_HAT);
        CULINARY_MASTERS_HAT_MESSAGE = commonBuilder
                .comment("This boolean value corresponds to whether culinary masters hat message is enabled.")
                .worldRestart()
                .translation(MOD_ID + ".config.culinary_masters_hat_message")
                .define("enabledCulinaryMastersHatMessage", true);
        commonBuilder.pop();

        commonBuilder.pop();

        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();

        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();

        clientBuilder.comment(TOOLTIP).push(TOOLTIP);
        SHOW_NUTRIENT_VALUE_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the nutrient value tooltip.")
                .translation(MOD_ID + ".config.show_nutrient_value_tooltip")
                .define("showNutrientValueTooltip", true);
        SHOW_ESTIMATED_VALUE_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the estimated value tooltip.")
                .translation(MOD_ID + ".config.show_estimated_value_tooltip")
                .define("showEstimatedValueTooltip", true);
        SHOW_ESTIMATED_BUFF_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the estimated buff tooltip.")
                .translation(MOD_ID + ".config.show_estimated_buff_tooltip")
                .define("showEstimatedBuffTooltip", true);
        clientBuilder.pop();

        clientBuilder.comment(SOUND).push(SOUND);
        CASSEROLE_SOUND = clientBuilder
                .comment("This boolean value corresponds to whether to enable the casserole sound.")
                .translation(MOD_ID + ".config.casserole_sound")
                .define("casseroleSound", true);
        ROASTER_SOUND = clientBuilder
                .comment("This boolean value corresponds to whether to enable the roaster sound.")
                .translation(MOD_ID + ".config.roaster_sound")
                .define("roasterSound", true);
        clientBuilder.pop();

        clientBuilder.comment(OVERLAY).push(OVERLAY);
        POT_OVERLAY = clientBuilder
                .comment("This boolean value corresponds to whether to enable the pot overlay.")
                .translation(MOD_ID + ".config.pot_overlay")
                .define("potOverlay", true);
        clientBuilder.pop();

        CLIENT_CONFIG = clientBuilder.build();
    }
}
