package mangopill.customized.common.util;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.recipe.serializer.PropertyValueSerializer;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.util.value.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public final class PropertyValueUtil {

    private PropertyValueUtil() {
    }

    public static PropertyValue getPropertyValue(ItemStack stack, Level level) {
        List<PropertyValueRecipe> recipes = level.getRecipeManager().getRecipesFor(CRecipeRegistry.PROPERTY_VALUE.get(), new SingleRecipeInput(stack), level).stream().map(RecipeHolder::value).toList();
        return getPropertyValue(stack, recipes);
    }

    public static PropertyValue getPropertyValue(ItemStack stack, List<PropertyValueRecipe> recipes) {
        if (recipes.isEmpty()) {
            return new PropertyValue();
        }
        Optional<PropertyValue> directMatch = recipes.stream().flatMap(recipe -> recipe.groups().stream())
                .filter(group -> group.items().contains(BuiltInRegistries.ITEM.getKey(stack.getItem())))
                .findFirst().map(PropertyValueSerializer.PropertyValueGroup::propertyValue);
        if (directMatch.isPresent()) {
            return directMatch.get();
        }
        Map<ResourceLocation, PropertyValue> tagValueMap = recipes.stream().flatMap(recipe -> recipe.groups().stream())
                .flatMap(group -> group.tags().stream().map(tag -> Map.entry(tag, group.propertyValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
        return stack.getTags().map(TagKey::location).filter(tagValueMap::containsKey)
                .max(Comparator.comparingLong(tag -> tag.getPath().chars().filter(c -> c == '/').count()))
                .map(tagValueMap::get).orElse(new PropertyValue());
    }

    public static boolean matchesGroup(ItemStack stack, PropertyValueSerializer.PropertyValueGroup group) {
        if (group.items().contains(BuiltInRegistries.ITEM.getKey(stack.getItem()))) {
            return true;
        }
        return stack.getTags().map(TagKey::location).anyMatch(group.tags()::contains);
    }

    public static FoodProperties getFoodPropertyByPropertyValue(Level level, List<ItemStack> stackList, boolean shardByConsumption) {
        return getFoodPropertyByPropertyValue(level, stackList, null, shardByConsumption);
    }

    public static FoodProperties getFoodPropertyByPropertyValue(Level level, List<ItemStack> stackList, @Nullable Block block, boolean shardByConsumption) {
        if (stackList.isEmpty()) {
            return FoodValue.EMPTY;
        }
        Map<String, Float> nutrientTotal = new HashMap<>();
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        int nutritionValue = 0;
        float saturationValue = 0.0F;
        for (ItemStack stack : stackList) {
            PropertyValue propertyValue = getPropertyValue(stack, level);
            FoodProperties food = stack.getFoodProperties(null);
            if (food != null && !food.effects().isEmpty()) {
                foodEffect.addAll(food.effects());
            }
            if (!propertyValue.isEmpty()) {
                propertyValue.getValue().forEach((category, value) -> nutrientTotal.merge(category, value * stack.getCount(), Float::sum));
            } else {
                if (food == null) return FoodValue.INEDIBLE;
                nutritionValue += food.nutrition() * stack.getCount();
                saturationValue += food.saturation() * stack.getCount();
            }
        }
        for (Map.Entry<String, Float> entry : nutrientTotal.entrySet()) {
            String category = entry.getKey();
            float value = entry.getValue();
            for (RecipeHolder<NutrientCategoryRecipe> recipeHolder : level.getRecipeManager().getAllRecipesFor(CRecipeRegistry.NUTRIENT_CATEGORY.get())) {
                if (category.equals(recipeHolder.value().name())) {
                    nutritionValue += Math.round(value * recipeHolder.value().nutrition());
                    saturationValue += value * recipeHolder.value().saturation();
                }
            }
        }
        nutritionValue = Math.max(0, (int) (nutritionValue - nutritionValue * getShrinkData(nutrientTotal, level).shrinkNutrition));
        saturationValue = Math.max(0.0F, saturationValue - saturationValue * getShrinkData(nutrientTotal, level).shrinkSaturation);
        FoodProperties.Builder builder = new FoodProperties.Builder();
        foodEffect.addAll(getCustomizedFoodEffectList(nutrientTotal, level, block));
        int consumptionCount = getConsumptionCount(stackList);
        builder.nutrition(shardByConsumption ? new BigDecimal(nutritionValue).divide(BigDecimal.valueOf(consumptionCount), 2, RoundingMode.HALF_UP).intValue() : nutritionValue);
        BigDecimal saturationCalc = new BigDecimal(saturationValue).divide(BigDecimal.valueOf(2.0F), 6, RoundingMode.HALF_UP);
        builder.saturationModifier(shardByConsumption ? saturationCalc.divide(BigDecimal.valueOf(consumptionCount), 6, RoundingMode.HALF_UP).floatValue() : saturationCalc.floatValue());
        if (!foodEffect.isEmpty()) {
            foodEffect.forEach(pair -> builder.effect(pair.effectSupplier(), pair.probability()));
        }
        return builder.alwaysEdible().build();
    }

    public static List<FoodProperties.PossibleEffect> getCustomizedFoodEffectList(Map<String, Float> nutrientTotal, Level level, @Nullable Block block) {
        Map<String, Float> filteredNutrientTotal = getFilteredNutrientTotal(nutrientTotal);
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        getMatchedBuffRecipes(filteredNutrientTotal, level).forEach(matched -> addBuffToList(matched.getSecond(), foodEffect, matched.getFirst(), block));
        return foodEffect;
    }

    public static Map<String, Float> getFilteredNutrientTotal(Map<String, Float> nutrientTotal) {
        return nutrientTotal.entrySet().stream().filter(entry -> entry.getValue() > 0.0F).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<Pair<NutrientBuffRecipe, Float>> getMatchedBuffRecipes(Map<String, Float> filteredNutrientTotal, Level level) {
        return level.getRecipeManager().getAllRecipesFor(CRecipeRegistry.NUTRIENT_BUFF.get()).stream()
                .map(RecipeHolder::value).map(recipe -> Pair.of(recipe, checkRecipeMatch(filteredNutrientTotal, recipe.nutrientCategory())))
                .filter(pair -> pair.getSecond() > 0.0F).toList();
    }

    public static Float checkRecipeMatch(Map<String, Float> filteredNutrientTotal, List<HashSet<Pair<String, Float>>> nutrientCategories) {
        return nutrientCategories.stream().map(categorySet -> {
            float totalValue = 0.0F;
            for (Pair<String, Float> requirement : categorySet) {
                Float actualValue = filteredNutrientTotal.get(requirement.getFirst());
                if (actualValue == null || actualValue < requirement.getSecond()) {
                    return null;
                }
                totalValue += actualValue;
            }
            return totalValue;
        }).filter(Objects::nonNull).findFirst().orElse(0.0F);
    }

    public static ShrinkData getShrinkData(Map<String, Float> nutrientTotal, Level level) {
        Map<String, Float> filteredNutrientTotal = getFilteredNutrientTotal(nutrientTotal);
        List<Pair<NutrientBuffRecipe, Float>> matchedRecipes = getMatchedBuffRecipes(filteredNutrientTotal, level);
        float shrinkNutritionTotal = 0.0F;
        float shrinkSaturationTotal = 0.0F;
        for (Pair<NutrientBuffRecipe, Float> matched : matchedRecipes) {
            NutrientBuffRecipe recipe = matched.getFirst();
            shrinkNutritionTotal += recipe.shrinkNutrition();
            shrinkSaturationTotal += recipe.shrinkSaturation();
        }
        return new ShrinkData(shrinkNutritionTotal, shrinkSaturationTotal);
    }

    public static List<RecipeHolder<NutrientCategoryRecipe>> getNutrientCategoryByName(Level level, String name) {
        return level.getRecipeManager().getAllRecipesFor(CRecipeRegistry.NUTRIENT_CATEGORY.get()).stream()
                .filter(recipeHolder -> recipeHolder.value().name().equals(name)).toList();
    }

    public record ShrinkData(float shrinkNutrition, float shrinkSaturation) {}

    public static void addBuffToList(float nutrientValue, List<FoodProperties.PossibleEffect> foodEffect, NutrientBuffRecipe recipe, @Nullable Block block) {
        if (block != null && !recipe.pot().isEmpty()) {
            boolean potMatches = recipe.pot().stream()
                    .anyMatch(ingredient -> ingredient.test(block.asItem().getDefaultInstance()));
            if (!potMatches) {
                return;
            }
        }
        int duration = Math.round(nutrientValue * recipe.duration());
        float probability = nutrientValue * recipe.probability();
        int amplifier = Math.min((int) (nutrientValue / BUFF_AMPLIFIER.get()), 9);
        foodEffect.add(new FoodProperties.PossibleEffect(() -> new MobEffectInstance(recipe.effect(), duration, amplifier), Math.min(probability, 1.0F)));
    }
}
