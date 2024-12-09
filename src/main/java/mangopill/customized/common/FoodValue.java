package mangopill.customized.common;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodValue {
    public static final FoodProperties NULL = new FoodProperties.Builder()
            .alwaysEdible().build();
    public static final FoodProperties INEDIBLE = new FoodProperties.Builder()
            .nutrition(4).saturationModifier(0.5f)
            .build();

    public static final FoodProperties TOMATO = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.2f).build();
    public static final FoodProperties BITTER_GOURD = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.5f).build();
    public static final FoodProperties BROAD_BEAN = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.2f).effect(() -> new MobEffectInstance(MobEffects.POISON, 50, 0), 0.4F).build();

    public static final FoodProperties KETCHUP = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.5f).build();
}
