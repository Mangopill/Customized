package mangopill.customized.common;

import mangopill.customized.Customized;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CustomizedConfig {
    public static final String MOD_ID = Customized.MODID;
    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec CLIENT_CONFIG;

    public static final String NUTRIENT = "nutrient";
    public static final String NUTRITION = "nutrition";
    public static final ModConfigSpec.DoubleValue WATER_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue PROTEIN_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue LIPID_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue CARBOHYDRATE_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue VITAMIN_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue MINERAL_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue DIETARY_FIBER_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue COLD_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue WARM_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue ECOLOGY_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue DREAD_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue NOTHINGNESS_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue SOUR_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue SWEET_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue BITTER_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue SPICY_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue SALTY_NUTRITION_VALUE;
    public static final ModConfigSpec.DoubleValue NUMBING_NUTRITION_VALUE;
    public static final String SATURATION = "saturation";
    public static final ModConfigSpec.DoubleValue WATER_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue PROTEIN_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue LIPID_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue CARBOHYDRATE_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue VITAMIN_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue MINERAL_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue DIETARY_FIBER_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue COLD_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue WARM_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue ECOLOGY_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue DREAD_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue NOTHINGNESS_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue SOUR_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue SWEET_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue BITTER_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue SPICY_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue SALTY_SATURATION_VALUE;
    public static final ModConfigSpec.DoubleValue NUMBING_SATURATION_VALUE;
    public static final String BUFF = "buff";
    public static final ModConfigSpec.BooleanValue NORMAL_BUFF;
    public static final ModConfigSpec.DoubleValue NORMAL_BUFF_DURATION;
    public static final ModConfigSpec.DoubleValue NORMAL_BUFF_PROBABILITY;
    public static final ModConfigSpec.BooleanValue POWERFUL_BUFF;
    public static final ModConfigSpec.DoubleValue POWERFUL_BUFF_DURATION;
    public static final ModConfigSpec.DoubleValue POWERFUL_BUFF_PROBABILITY;
    public static final ModConfigSpec.BooleanValue COMBINATION_BUFF;
    public static final ModConfigSpec.DoubleValue COMBINATION_BUFF_DURATION;
    public static final ModConfigSpec.DoubleValue COMBINATION_BUFF_PROBABILITY;
    public static final String POT = "pot";
    public static final ModConfigSpec.BooleanValue CUSTOM_COOKING;
    public static final ModConfigSpec.BooleanValue RECIPE_COOKING;

    public static final String TOOLTIP = "tooltip";
    public static final ModConfigSpec.BooleanValue SHOW_NUTRIENT_VALUE_TOOLTIP;
    public static final ModConfigSpec.BooleanValue SHOW_ESTIMATED_VALUE_TOOLTIP;
    public static final ModConfigSpec.BooleanValue SHOW_ESTIMATED_BUFF_TOOLTIP;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();

        commonBuilder.comment(NUTRIENT).push(NUTRIENT);

        commonBuilder.comment(NUTRITION).push(NUTRITION);
        WATER_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.water_nutrition_value")
                .defineInRange("waterNutritionValue", 0.000D, 0.0D, 100.0D);
        PROTEIN_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.005D")
                .gameRestart()
                .translation(MOD_ID + ".config.protein_nutrition_value")
                .defineInRange("proteinNutritionValue", 0.005D, 0.0D, 100.0D);
        LIPID_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.002D")
                .gameRestart()
                .translation(MOD_ID + ".config.lipid_nutrition_value")
                .defineInRange("lipidNutritionValue", 0.002D, 0.0D, 100.0D);
        CARBOHYDRATE_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.001D")
                .gameRestart()
                .translation(MOD_ID + ".config.carbohydrate_nutrition_value")
                .defineInRange("carbohydrateNutritionValue", 0.001D, 0.0D, 100.0D);
        VITAMIN_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.vitamin_nutrition_value")
                .defineInRange("vitaminNutritionValue", 0.000D, 0.0D, 100.0D);
        MINERAL_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.001D")
                .gameRestart()
                .translation(MOD_ID + ".config.mineral_nutrition_value")
                .defineInRange("mineralNutritionValue", 0.001D, 0.0D, 100.0D);
        DIETARY_FIBER_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.003D")
                .gameRestart()
                .translation(MOD_ID + ".config.dietary_fiber_nutrition_value")
                .defineInRange("dietaryFiberNutritionValue", 0.003D, 0.0D, 100.0D);
        COLD_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.cold_nutrition_value")
                .defineInRange("coldNutritionValue", 0.000D, 0.0D, 100.0D);
        WARM_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.warm_nutrition_value")
                .defineInRange("warmNutritionValue", 0.000D, 0.0D, 100.0D);
        ECOLOGY_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.ecology_nutrition_value")
                .defineInRange("ecologyNutritionValue", 0.000D, 0.0D, 100.0D);
        DREAD_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.dread_nutrition_value")
                .defineInRange("dreadNutritionValue", 0.000D, 0.0D, 100.0D);
        NOTHINGNESS_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.nothingness_nutrition_value")
                .defineInRange("nothingnessNutritionValue", 0.000D, 0.0D, 100.0D);
        SOUR_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.sour_nutrition_value")
                .defineInRange("sourNutritionValue", 0.000D, 0.0D, 100.0D);
        SWEET_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.sweet_nutrition_value")
                .defineInRange("sweetNutritionValue", 0.000D, 0.0D, 100.0D);
        BITTER_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.bitter_nutrition_value")
                .defineInRange("bitterNutritionValue", 0.000D, 0.0D, 100.0D);
        SPICY_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.spicy_nutrition_value")
                .defineInRange("spicyNutritionValue", 0.000D, 0.0D, 100.0D);
        SALTY_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.salty_nutrition_value")
                .defineInRange("saltyNutritionValue", 0.000D, 0.0D, 100.0D);
        NUMBING_NUTRITION_VALUE = commonBuilder
                .comment("This value corresponds to the nutrition provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.numbing_nutrition_value")
                .defineInRange("numbingNutritionValue", 0.000D, 0.0D, 100.0D);
        commonBuilder.pop();

        commonBuilder.comment(SATURATION).push(SATURATION);
        WATER_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.003D")
                .gameRestart()
                .translation(MOD_ID + ".config.water_saturation_value")
                .defineInRange("waterSaturationValue", 0.003D, 0.0D, 100.0D);
        PROTEIN_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.protein_saturation_value")
                .defineInRange("proteinSaturationValue", 0.000D, 0.0D, 100.0D);
        LIPID_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.002D")
                .gameRestart()
                .translation(MOD_ID + ".config.lipid_saturation_value")
                .defineInRange("lipidSaturationValue", 0.002D, 0.0D, 100.0D);
        CARBOHYDRATE_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.004D")
                .gameRestart()
                .translation(MOD_ID + ".config.carbohydrate_saturation_value")
                .defineInRange("carbohydrateSaturationValue", 0.004D, 0.0D, 100.0D);
        VITAMIN_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.001D")
                .gameRestart()
                .translation(MOD_ID + ".config.vitamin_saturation_value")
                .defineInRange("vitaminSaturationValue", 0.001D, 0.0D, 100.0D);
        MINERAL_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.mineral_saturation_value")
                .defineInRange("mineralSaturationValue", 0.000D, 0.0D, 100.0D);
        DIETARY_FIBER_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.dietary_fiber_saturation_value")
                .defineInRange("dietaryFiberSaturationValue", 0.000D, 0.0D, 100.0D);
        COLD_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.cold_saturation_value")
                .defineInRange("coldSaturationValue", 0.000D, 0.0D, 100.0D);
        WARM_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.warm_saturation_value")
                .defineInRange("warmSaturationValue", 0.000D, 0.0D, 100.0D);
        ECOLOGY_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.ecology_saturation_value")
                .defineInRange("ecologySaturationValue", 0.000D, 0.0D, 100.0D);
        DREAD_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.dread_saturation_value")
                .defineInRange("dreadSaturationValue", 0.000D, 0.0D, 100.0D);
        NOTHINGNESS_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.nothingness_saturation_value")
                .defineInRange("nothingnessSaturationValue", 0.000D, 0.0D, 100.0D);
        SOUR_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.sour_saturation_value")
                .defineInRange("sourSaturationValue", 0.000D, 0.0D, 100.0D);
        SWEET_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.sweet_saturation_value")
                .defineInRange("sweetSaturationValue", 0.000D, 0.0D, 100.0D);
        BITTER_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.bitter_saturation_value")
                .defineInRange("bitterSaturationValue", 0.000D, 0.0D, 100.0D);
        SPICY_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.spicy_saturation_value")
                .defineInRange("spicySaturationValue", 0.000D, 0.0D, 100.0D);
        SALTY_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.salty_saturation_value")
                .defineInRange("saltySaturationValue", 0.000D, 0.0D, 100.0D);
        NUMBING_SATURATION_VALUE = commonBuilder
                .comment("This value corresponds to the saturation provided by every 0.1% of this nutrient value.\n(0.0D, 100.0D)\ndefault: 0.000D")
                .gameRestart()
                .translation(MOD_ID + ".config.numbing_saturation_value")
                .defineInRange("numbingSaturationValue", 0.000D, 0.0D, 100.0D);
        commonBuilder.pop();

        commonBuilder.comment(BUFF).push(BUFF);
        NORMAL_BUFF = commonBuilder
                .comment("This boolean value corresponds to whether the normal buff effects in custom food are enabled.\n(true, false)\ndefault: true")
                .worldRestart()
                .translation(MOD_ID + ".config.normal_buff")
                .define("enabledNormalBuff", true);
        NORMAL_BUFF_DURATION = commonBuilder
                .comment("This value determines the duration of the normal buff for every 0.1% of the nutrient value.\n(0.0D, 100.0D)\ndefault: 0.01D")
                .gameRestart()
                .translation(MOD_ID + ".config.normal_buff_duration")
                .defineInRange("normalBuffDuration", 0.1D, 0.0D, 100.0D);
        NORMAL_BUFF_PROBABILITY = commonBuilder
                .comment("This value corresponds to the normal buff probability for each 0.1% of the nutrition value.\n(0.0D, 100.0D)\ndefault: 0.0003D")
                .gameRestart()
                .translation(MOD_ID + ".config.normal_buff_probability")
                .defineInRange("normalBuffProbability", 0.0003D, 0.0D, 100.0D);
        POWERFUL_BUFF = commonBuilder
                .comment("This boolean value corresponds to whether the powerful buff effects in custom food are enabled.\n(true, false)\ndefault: true")
                .worldRestart()
                .translation(MOD_ID + ".config.powerful_buff")
                .define("enabledPowerfulBuff", true);
        POWERFUL_BUFF_DURATION = commonBuilder
                .comment("This value determines the duration of the powerful buff for every 0.1% of the nutrient value.\n(0.0D, 100.0D)\ndefault: 0.01D")
                .gameRestart()
                .translation(MOD_ID + ".config.powerful_buff_duration")
                .defineInRange("powerfulBuffDuration", 0.1D, 0.0D, 100.0D);
        POWERFUL_BUFF_PROBABILITY = commonBuilder
                .comment("This value corresponds to the powerful buff probability for each 0.1% of the nutrition value.\n(0.0D, 100.0D)\ndefault: 0.0002D")
                .gameRestart()
                .translation(MOD_ID + ".config.powerful_buff_probability")
                .defineInRange("powerfulBuffProbability", 0.0002D, 0.0D, 100.0D);
        COMBINATION_BUFF = commonBuilder
                .comment("This boolean value corresponds to whether the combination buff effects in custom food are enabled.\n(true, false)\ndefault: true")
                .worldRestart()
                .translation(MOD_ID + ".config.combination_buff")
                .define("enabledCombinationBuff", true);
        COMBINATION_BUFF_DURATION = commonBuilder
                .comment("This value determines the duration of the combination buff for every 0.1% of the nutrient value.\n(0.0D, 100.0D)\ndefault: 0.01D")
                .gameRestart()
                .translation(MOD_ID + ".config.combination_buff_duration")
                .defineInRange("combinationBuffDuration", 0.1D, 0.0D, 100.0D);
        COMBINATION_BUFF_PROBABILITY = commonBuilder
                .comment("This value corresponds to the combination buff probability for each 0.1% of the nutrition value.\n(0.0D, 100.0D)\ndefault: 0.0001D")
                .gameRestart()
                .translation(MOD_ID + ".config.combination_buff_probability")
                .defineInRange("combinationBuffProbability", 0.0001D, 0.0D, 100.0D);
        commonBuilder.pop();

        commonBuilder.pop();

        commonBuilder.comment(POT).push(POT);
        CUSTOM_COOKING = commonBuilder
                .comment("This boolean value corresponds to whether custom cooking is enabled.\n(true, false)\ndefault: true")
                .worldRestart()
                .translation(MOD_ID + ".config.custom_cooking")
                .define("enabledCustomCooking", true);
        RECIPE_COOKING = commonBuilder
                .comment("This boolean value corresponds to whether recipe cooking is enabled.\n(true, false)\ndefault: true")
                .worldRestart()
                .translation(MOD_ID + ".config.recipe_cooking")
                .define("enabledRecipeCooking", true);
        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();

        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();

        clientBuilder.comment(TOOLTIP).push(TOOLTIP);
        SHOW_NUTRIENT_VALUE_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the nutrient value tooltip.\n(true, false)\ndefault: true")
                .translation(MOD_ID + ".config.show_nutrient_value_tooltip")
                .define("showNutrientValueTooltip", true);
        SHOW_ESTIMATED_VALUE_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the estimated value tooltip.\n(true, false)\ndefault: true")
                .translation(MOD_ID + ".config.show_estimated_value_tooltip")
                .define("showEstimatedValueTooltip", true);
        SHOW_ESTIMATED_BUFF_TOOLTIP = clientBuilder
                .comment("This boolean value corresponds to whether to show the estimated buff tooltip.\n(true, false)\ndefault: true")
                .translation(MOD_ID + ".config.show_estimated_buff_tooltip")
                .define("showEstimatedBuffTooltip", true);
        clientBuilder.pop();

        CLIENT_CONFIG = clientBuilder.build();
    }
}
