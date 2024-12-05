package mangopill.customized.common.util;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.effect.ShrinkNutritionMobEffect;
import mangopill.customized.common.effect.ShrinkSaturationMobEffect;
import mangopill.customized.common.effect.CombinationMobEffect;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.category.NutrientCategory.*;
import static mangopill.customized.common.util.value.NutrientBuff.*;

public final class PropertyValueUtil {
    private PropertyValueUtil() {
    }
    @NotNull
    public static PropertyValue getPropertyValue(ItemStack stack, Level level) {
        List<RecipeHolder<PropertyValueRecipe>> recipeHolder = level.getRecipeManager().getRecipesFor(ModRecipeRegistry.PROPERTY_VALUE.get(), new SingleRecipeInput(stack), level);
        if (recipeHolder.isEmpty()) {
            return new PropertyValue();
        }
        return recipeHolder.stream()
                .map(RecipeHolder::value)
                .filter(PropertyValueRecipe::isItem)
                .findFirst().map(PropertyValueRecipe::getPropertyValue)
                .orElseGet(() -> {
                    PropertyValue propertyValue = new PropertyValue();
                    long maxCount = 0L;
                    HashMap<ResourceLocation, PropertyValue> map = new HashMap<>();
                    recipeHolder.stream().map(RecipeHolder::value).forEach(valueRecipe ->
                            valueRecipe.getName().forEach(name -> map.put(name, valueRecipe.getPropertyValue()))
                    );
                    for (ResourceLocation tag : stack.getTags().map(TagKey::location).filter(map::containsKey).toList()) {
                        long count = tag.getPath().chars().filter(c -> c == '/').count();
                        if (count >= maxCount) {
                            if (count > maxCount) {
                                maxCount = count;
                                propertyValue.replace();
                            }
                            map.get(tag).toSet().forEach(entry ->
                                    propertyValue.put(entry.getKey(),
                                            Math.max(propertyValue.getBigger(entry.getKey()), entry.getValue()))
                            );
                        }
                    }
                    return propertyValue;
                });
    }

    public static FoodProperties getFoodPropertyByPropertyValue(Level level, List<ItemStack> stackList, boolean shardByConsumption) {
        if (stackList == null || stackList.isEmpty()) {
            return FoodValue.NULL;
        }
        Map<NutrientCategory, Float> nutrientTotal = new EnumMap<>(NutrientCategory.class);
        for (NutrientCategory category : NutrientCategory.values()) {
            nutrientTotal.put(category, 0.0F);
        }
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        for (ItemStack stack : stackList) {
            @NotNull PropertyValue propertyValue = getPropertyValue(stack, level);
            FoodProperties food = stack.getFoodProperties(null);
            if (food != null) {
                if (!food.effects().isEmpty()) {
                    foodEffect.addAll(food.effects());
                }
            }
            if (!propertyValue.isEmpty()) {
                for (Pair<NutrientCategory, Float> entry : propertyValue.toSet()) {
                    NutrientCategory category = entry.getKey();
                    float value = entry.getValue() * stack.getCount();
                    nutrientTotal.put(category, nutrientTotal.get(category) + value);
                }
            } else {
                return FoodValue.INEDIBLE;
            }
        }
        int nutritionValue = 0;
        float saturationValue = 0.0F;
        for (NutrientCategory category : nutrientTotal.keySet()){
            for (NutrientFoodValue value : NutrientFoodValue.values()) {
                if (category.name().equals(value.name())){
                    nutritionValue += (int) Math.round((nutrientTotal.get(category) * 10 * value.getNutrition()));
                    saturationValue += (float) (nutrientTotal.get(category) * 10.0F * value.getSaturation());
                }
            }
        }
        if (COMBINATION_BUFF.get()){
            nutritionValue = Math.max(0, (int) (nutritionValue - nutritionValue * getShrinkNutrition(nutrientTotal)));
            saturationValue = Math.max(0.0F, saturationValue - saturationValue * getShrinkSaturation(nutrientTotal));
        }
        FoodProperties.Builder builder = new FoodProperties.Builder();
        foodEffect.addAll(getCustomizedFoodEffectList(nutrientTotal));
        if (shardByConsumption){
            int consumptionCount = getConsumptionCount(stackList);
            if (consumptionCount != 0){
                builder.nutrition(nutritionValue / consumptionCount);
            } else {
                builder.nutrition(0);
            }
            if (nutritionValue * 2L != 0){
                BigDecimal newSaturationValue = new BigDecimal(saturationValue)
                        .divide(BigDecimal.valueOf(nutritionValue * 2L), 6, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(consumptionCount), 5, RoundingMode.HALF_UP);
                builder.saturationModifier(newSaturationValue.floatValue() * 6);
            } else {
                builder.saturationModifier(0.0F);
            }
            if (!foodEffect.isEmpty()) {
                for (FoodProperties.PossibleEffect pair : foodEffect) {
                    builder.effect(pair.effectSupplier(), pair.probability());
                }
            }
        } else {
            builder.nutrition(nutritionValue);
            if (nutritionValue * 2L != 0){
                BigDecimal newSaturationValue = new BigDecimal(saturationValue)
                        .divide(BigDecimal.valueOf(nutritionValue * 2L), 5, RoundingMode.HALF_UP);
                builder.saturationModifier(newSaturationValue.floatValue() * 6);
            } else {
                builder.saturationModifier(0.0F);
            }
            if (!foodEffect.isEmpty()) {
                for (FoodProperties.PossibleEffect pair : foodEffect) {
                    builder.effect(pair.effectSupplier(), pair.probability());
                }
            }
        }
        return builder.alwaysEdible().build();
    }

    public static List<FoodProperties.PossibleEffect> getCustomizedFoodEffectList(Map<NutrientCategory, Float> nutrientTotal){
        Map<NutrientCategory, Float> filteredNutrientTotal = getFilteredNutrientTotal(nutrientTotal);
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        for (NutrientCategory category : filteredNutrientTotal.keySet()){
            float nutrientValue = filteredNutrientTotal.get(category);
            if (NORMAL_BUFF.get()) {
                addBuffToList(nutrientValue, foodEffect, getNormalBuff(category));
            }
            if (POWERFUL_BUFF.get()) {
                addBuffToList(nutrientValue, foodEffect, getPowerfulBuff(category));
            }
        }
        if (COMBINATION_BUFF.get()) {
            Map<NutrientBuff, Float> combinationBuff = getCombinationBuffMap(filteredNutrientTotal);
            combinationBuff.forEach((n, f) -> addBuffToList(f, foodEffect, n));
        }
        return foodEffect;
    }

    public static @NotNull Map<NutrientCategory, Float> getFilteredNutrientTotal(Map<NutrientCategory, Float> nutrientTotal) {
        return nutrientTotal.entrySet().stream()
                .filter(entry -> entry.getValue() > 0.0F)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<NutrientBuff, Float> getCombinationBuffMap(Map<NutrientCategory, Float> filteredNutrientTotal) {
        Map<NutrientBuff, Float> combinationBuff = new HashMap<>();
        Set<NutrientBuff> nutrientTotal = EnumSet.allOf(NutrientBuff.class);
        Set<NutrientCategory> saturation1 = Set.of(WATER, PROTEIN);
        Set<NutrientCategory> saturation2 = Set.of(WATER, DIETARY_FIBER);
        putCombinationBuffByContains(filteredNutrientTotal, combinationBuff, NutrientBuff.SATURATION, List.of(saturation1, saturation2));
        nutrientTotal.forEach(nutrientBuff -> {
            if (nutrientBuff.getEffect().value() instanceof CombinationMobEffect combinationMobEffect) {
                putCombinationBuffByContains(filteredNutrientTotal, combinationBuff, nutrientBuff, combinationMobEffect.getCategorySet());
            }
        });
        return combinationBuff;
    }

    public static float getShrinkNutrition(Map<NutrientCategory, Float> nutrientTotal) {
        Map<NutrientBuff, Float> buffMap = getCombinationBuffMap(getFilteredNutrientTotal(nutrientTotal));
        float i = 0.0F;
        for (NutrientBuff nutrient : buffMap.keySet()) {
            if (nutrient.getEffect().value() instanceof ShrinkNutritionMobEffect effect) {
                i += effect.getShrinkNutritionModifier();
            }
        }
        return i;
    }

    public static float getShrinkSaturation(Map<NutrientCategory, Float> nutrientTotal) {
        Map<NutrientBuff, Float> buffMap = getCombinationBuffMap(getFilteredNutrientTotal(nutrientTotal));
        float i = 0.0F;
        for (NutrientBuff nutrient : buffMap.keySet()) {
            if (nutrient.getEffect().value() instanceof ShrinkSaturationMobEffect effect) {
                i += effect.getShrinkSaturationModifier();
            }
        }
        return i;
    }

    public static void putCombinationBuffByContains(Map<NutrientCategory, Float> filteredNutrientTotal, Map<NutrientBuff, Float> combinationBuffMap, NutrientBuff combinationBuff, List<Set<NutrientCategory>> sets) {
        for (Set<NutrientCategory> set : sets) {
            if (filteredNutrientTotal.keySet().containsAll(set)) {
                float totalValue = (float) filteredNutrientTotal.entrySet().stream()
                        .filter(entry -> set.contains(entry.getKey()))
                        .mapToDouble(Map.Entry::getValue)
                        .sum();
                combinationBuffMap.put(combinationBuff, totalValue);
            }
        }
    }

    public static void addBuffToList(float nutrientValue, List<FoodProperties.PossibleEffect> foodEffect, NutrientBuff nutrientBuff) {
        if (nutrientBuff == null){
            return;
        }
        int duration = Math.round(nutrientValue * 10 * (float) nutrientBuff.getDuration());
        float probability = (float) (nutrientValue * 10 * nutrientBuff.getProbability());
        foodEffect.add(new FoodProperties.PossibleEffect(
                () -> new MobEffectInstance(nutrientBuff.getEffect(), duration, 0), Math.min(probability, 1.0F)));
    }

    public static @Nullable NutrientBuff getNormalBuff(NutrientCategory category){
        return switch (category) {
            case COLD -> ICED;
            case WARM -> WARM_STOMACH;
            default -> null;
        };
    }

    public static @Nullable NutrientBuff getPowerfulBuff(NutrientCategory category){
        return switch (category) {
            case ECOLOGY -> VITALITY;
            case DREAD -> ANTIDOTE;
            case NOTHINGNESS -> SOAR;
            default -> null;
        };
    }
}
