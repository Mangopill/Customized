package mangopill.customized.common;

import mangopill.customized.common.registry.CEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class FoodValue {
    public static final FoodProperties NULL = new FoodProperties.Builder()
            .alwaysEdible().build();
    public static final FoodProperties INEDIBLE = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.5F)
            .build();

    public static final int SHORT = 2400;
    public static final int MEDIUM = 4800;

    public static final FoodProperties TOMATO = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.2F).build();
    public static final FoodProperties BITTER_GOURD = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.5F).build();
    public static final FoodProperties BROAD_BEAN = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.2F).effect(() -> new MobEffectInstance(MobEffects.POISON, 50, 0), 0.4F).build();
    public static final FoodProperties CHILLI = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.4F).build();
    public static final FoodProperties SOYBEAN = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.3F).effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 0), 0.6F).build();
    public static final FoodProperties SCALLION = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.2F).build();
    public static final FoodProperties GINGER = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.1F).build();

    public static final FoodProperties KETCHUP = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.5F).usingConvertsTo(Items.GLASS_BOTTLE).build();
    public static final FoodProperties VINEGAR = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(1.0F).usingConvertsTo(Items.GLASS_BOTTLE).build();
    public static final FoodProperties DOUBAN = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.8F).usingConvertsTo(Items.GLASS_BOTTLE).build();
    public static final FoodProperties SOY_SAUCE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.8F).usingConvertsTo(Items.GLASS_BOTTLE).build();
    public static final FoodProperties OYSTER_SAUCE = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.9F).usingConvertsTo(Items.GLASS_BOTTLE).build();

    public static final FoodProperties TOMATO_AND_BEEF_BRISKET_SOUP = new FoodProperties.Builder()
            .nutrition(18).saturationModifier(1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.SATURATION, MEDIUM, 1), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.ROBUST, MEDIUM, 2), 1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, SHORT, 2), 1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, MEDIUM, 2), 1.0F).build();
    public static final FoodProperties FISH_MAW_AND_CHICKEN_SOUP = new FoodProperties.Builder()
            .nutrition(16).saturationModifier(1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.SATURATION, MEDIUM, 1), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.VITALITY_RESTORATION, SHORT, 2), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.SUSTAINED_ENERGY, MEDIUM, 2), 1.0F).build();
    public static final FoodProperties BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP = new FoodProperties.Builder()
            .nutrition(16).saturationModifier(1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.SATURATION, MEDIUM, 1), 1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.REGENERATION, SHORT, 2), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.SMOOTH_BLOOD_FLOW, MEDIUM, 2), 1.0F).build();
    public static final FoodProperties RADISH_AND_PORK_RIB_SOUP = new FoodProperties.Builder()
            .nutrition(17).saturationModifier(1.0F).effect(
                    () -> new MobEffectInstance(MobEffects.SATURATION, MEDIUM, 1), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.HYDRATION_AND_PLUMPNESS, MEDIUM, 1), 1.0F).effect(
                    () -> new MobEffectInstance(CEffectRegistry.MENTAL_STIMULATION, MEDIUM, 2), 1.0F).build();
}
