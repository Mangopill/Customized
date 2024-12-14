package mangopill.customized.common;

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
}
