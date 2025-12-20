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
import static mangopill.customized.common.util.RecipeUtil.*;

public final class PropertyValueUtil {
    private PropertyValueUtil() {
    }

    /**
     * Gets the property value of an ItemStack based on registered recipes in the level.
     * <p>
     * This method searches for property value recipes that match the item, either by direct item ID
     * or by tags. If multiple tags match, the one with the deepest path (most forward slashes) is selected.
     * @see #getPropertyValue(ItemStack, List)
     * @param stack The ItemStack to get the property value for
     * @param level The level used to access the recipe manager
     * @return The property value of the item, or an empty PropertyValue if no match is found
     */
    public static PropertyValue getPropertyValue(ItemStack stack, Level level) {
        List<PropertyValueRecipe> recipes = getRecipeListFor(CRecipeRegistry.PROPERTY_VALUE.get(), new SingleRecipeInput(stack), level);
        return getPropertyValue(stack, recipes);
    }

    /**
     * Gets the property value of an ItemStack from a list of property value recipes.
     * <p>
     * This method checks if the item matches any group in the recipes by item ID or tags.
     * Tag matching uses the tag with the deepest path in case of multiple matches.
     * @see #getPropertyValue(ItemStack, Level)
     * @param stack The ItemStack to get the property value for
     * @param recipes The list of property value recipes to search through
     * @return The property value of the item, or an empty PropertyValue if no match is found
     */
    public static PropertyValue getPropertyValue(ItemStack stack, List<PropertyValueRecipe> recipes) {
        if (recipes.isEmpty()) return new PropertyValue();
        Optional<PropertyValue> directMatch = recipes.stream().flatMap(recipe -> recipe.groups().stream())
                .filter(group -> group.items().contains(BuiltInRegistries.ITEM.getKey(stack.getItem())))
                .findFirst().map(PropertyValueSerializer.PropertyValueGroup::propertyValue);
        if (directMatch.isPresent()) return directMatch.get();
        Map<ResourceLocation, PropertyValue> tagValueMap = recipes.stream().flatMap(recipe -> recipe.groups().stream())
                .flatMap(group -> group.tags().stream().map(tag -> Map.entry(tag, group.propertyValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
        return stack.getTags().map(TagKey::location).filter(tagValueMap::containsKey)
                .max(Comparator.comparingLong(tag -> tag.getPath().chars().filter(c -> c == '/').count()))
                .map(tagValueMap::get).orElse(new PropertyValue());
    }

    /**
     * Checks if an ItemStack matches a property value group.
     * <p>
     * The item matches if its item ID is in the group's item list or if any of its tags
     * are in the group's tag list.
     * @param stack The ItemStack to check
     * @param group The property value group to match against
     * @return true if the item matches the group, false otherwise
     */
    public static boolean matchesGroup(ItemStack stack, PropertyValueSerializer.PropertyValueGroup group) {
        if (group.items().contains(BuiltInRegistries.ITEM.getKey(stack.getItem()))) return true;
        return stack.getTags().map(TagKey::location).anyMatch(group.tags()::contains);
    }

    /**
     * Calculates food properties based on the property values of a list of items.
     * <p>
     * This method aggregates property values, calculates nutrition and saturation,
     * applies shrink factors from buff recipes, and optionally shares the values
     * by consumption count.
     * @see #getFoodPropertyByPropertyValue(Level, List, Block, boolean)
     * @param level The level used to access recipe managers and nutrient categories
     * @param stackList The list of ItemStacks to calculate properties for
     * @param shardByConsumption If true, nutrition and saturation are divided by consumption count
     * @return The calculated FoodProperties
     */
    public static FoodProperties getFoodPropertyByPropertyValue(Level level, List<ItemStack> stackList, boolean shardByConsumption) {
        return getFoodPropertyByPropertyValue(level, stackList, null, shardByConsumption);
    }

    /**
     * Calculates food properties based on the property values of a list of items, with pot checking.
     * <p>
     * Similar to {@link #getFoodPropertyByPropertyValue(Level, List, boolean)} but also checks
     * if the block (pot) is valid for the buff recipes.
     * @param level The level used to access recipe managers and nutrient categories
     * @param stackList The list of ItemStacks to calculate properties for
     * @param block The block (pot) used for cooking, can be null
     * @param shardByConsumption If true, nutrition and saturation are divided by consumption count
     * @return The calculated FoodProperties
     */
    public static FoodProperties getFoodPropertyByPropertyValue(Level level, List<ItemStack> stackList, @Nullable Block block, boolean shardByConsumption) {
        if (stackList.isEmpty()) return FoodValue.EMPTY;
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
            for (NutrientCategoryRecipe recipeHolder : getAllRecipeList(CRecipeRegistry.NUTRIENT_CATEGORY.get(), level)) {
                if (category.equals(recipeHolder.name())) {
                    nutritionValue += Math.round(value * recipeHolder.nutrition());
                    saturationValue += value * recipeHolder.saturation();
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


    /**
     * Generates a list of food effects based on nutrient totals.
     * <p>
     * Filters out non-positive nutrient values, finds matching buff recipes,
     * and adds their effects to the list if the pot (if provided) matches.
     * @param nutrientTotal Map of nutrient categories to their total values
     * @param level The level used to access buff recipes
     * @param block The block (pot) used for cooking, can be null
     * @return List of possible food effects
     */
    public static List<FoodProperties.PossibleEffect> getCustomizedFoodEffectList(Map<String, Float> nutrientTotal, Level level, @Nullable Block block) {
        Map<String, Float> filteredNutrientTotal = getFilteredNutrientTotal(nutrientTotal);
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        getMatchedBuffRecipes(filteredNutrientTotal, level).forEach(matched -> addBuffToList(matched.getSecond(), foodEffect, matched.getFirst(), block));
        return foodEffect;
    }


    /**
     * Filters out nutrient categories with non-positive values.
     * @param nutrientTotal Map of nutrient categories to their total values
     * @return Filtered map containing only categories with positive values
     */
    public static Map<String, Float> getFilteredNutrientTotal(Map<String, Float> nutrientTotal) {
        return nutrientTotal.entrySet().stream().filter(entry -> entry.getValue() > 0.0F).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Finds buff recipes that match the filtered nutrient totals.
     * <p>
     * Each buff recipe contains a list of nutrient category requirements.
     * A recipe matches if all requirements are satisfied.
     * @param filteredNutrientTotal Filtered map of nutrient categories with positive values
     * @param level The level used to access buff recipes
     * @return List of pairs containing matched recipes and their total nutrient value
     */
    public static List<Pair<NutrientBuffRecipe, Float>> getMatchedBuffRecipes(Map<String, Float> filteredNutrientTotal, Level level) {
        return getAllRecipeList(CRecipeRegistry.NUTRIENT_BUFF.get(), level).stream()
                .map(recipe -> Pair.of(recipe, checkRecipeMatch(filteredNutrientTotal, recipe.nutrientCategory())))
                .filter(pair -> pair.getSecond() > 0.0F).toList();
    }

    /**
     * Checks if the nutrient totals satisfy any set of requirements in a nutrient category list.
     * <p>
     * Each nutrient category list contains multiple sets of requirements (HashSet).
     * Returns the total nutrient value for the first satisfied set, or 0.0F if none match.
     * @param filteredNutrientTotal Filtered map of nutrient categories with positive values
     * @param nutrientCategories List of requirement sets (each set must be fully satisfied)
     * @return Total nutrient value of the matched set, or 0.0F if no match
     */
    public static Float checkRecipeMatch(Map<String, Float> filteredNutrientTotal, List<HashSet<Pair<String, Float>>> nutrientCategories) {
        return nutrientCategories.stream().map(categorySet -> {
            float totalValue = 0.0F;
            for (Pair<String, Float> requirement : categorySet) {
                Float actualValue = filteredNutrientTotal.get(requirement.getFirst());
                if (actualValue == null || actualValue < requirement.getSecond()) return null;
                totalValue += actualValue;
            }
            return totalValue;
        }).filter(Objects::nonNull).findFirst().orElse(0.0F);
    }

    /**
     * Calculates shrink data (nutrition and saturation reduction) based on matched buff recipes.
     * @see ShrinkData
     * @param nutrientTotal Map of nutrient categories to their total values
     * @param level The level used to access buff recipes
     * @return ShrinkData containing total shrink factors for nutrition and saturation
     */
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

    /**
     * Record representing shrink data for nutrition and saturation.
     * @param shrinkNutrition Total shrink factor for nutrition (0.0-1.0)
     * @param shrinkSaturation Total shrink factor for saturation (0.0-1.0)
     */
    public record ShrinkData(float shrinkNutrition, float shrinkSaturation) {}

    /**
     * Gets nutrient category recipes by name.
     *
     * @param level The level used to access nutrient category recipes
     * @param name The name of the nutrient category
     * @return List of RecipeHolders for the nutrient category with the given name
     */
    public static List<NutrientCategoryRecipe> getNutrientCategoryByName(Level level, String name) {
        return getAllRecipeList(CRecipeRegistry.NUTRIENT_CATEGORY.get(), level).stream()
                .filter(recipe -> recipe.name().equals(name)).toList();
    }

    /**
     * Adds a buff effect to the food effect list if the pot (if provided) matches.
     * <p>
     * The effect's duration is scaled by nutrient value, probability is multiplied by nutrient value,
     * and amplifier is calculated based on nutrient value divided by configuration value.
     * @param nutrientValue The total nutrient value for this buff
     * @param foodEffect The list to add the effect to
     * @param recipe The buff recipe containing effect information
     * @param block The block (pot) used for cooking, can be null (if null, pot check is skipped)
     */
    public static void addBuffToList(float nutrientValue, List<FoodProperties.PossibleEffect> foodEffect, NutrientBuffRecipe recipe, @Nullable Block block) {
        if (block != null && !recipe.pot().isEmpty()) {
            boolean potMatches = recipe.pot().stream()
                    .anyMatch(ingredient -> ingredient.test(block.asItem().getDefaultInstance()));
            if (!potMatches) return;
        }
        int duration = Math.round(nutrientValue * recipe.duration());
        float probability = nutrientValue * recipe.probability();
        int amplifier = Math.min((int) (nutrientValue / BUFF_AMPLIFIER.get()), 9);
        foodEffect.add(new FoodProperties.PossibleEffect(() -> new MobEffectInstance(recipe.effect(), duration, amplifier), Math.min(probability, 1.0F)));
    }

    /**
     * Gets the color of the nutrient category with the highest total value.
     * <p>
     * Calculates the sum of each nutrient category from the item list,
     * then returns the color of the category with the highest sum.
     * If no categories are found, returns a default color.
     * @param level The level used to access nutrient category recipes
     * @param stackList The list of ItemStacks to analyze
     * @return The color (with alpha) of the dominant nutrient category, or a default blue color
     */
    public static int getMaxValueColor(Level level, Collection<ItemStack> stackList) {
        Map<String, Float> nutrientSums = new HashMap<>();
        for (ItemStack stack : stackList) {
            PropertyValue propertyValue = getPropertyValue(stack, level);
            if (propertyValue.isEmpty()) continue;
            propertyValue.getValue().forEach((category, value) -> nutrientSums.put(category, nutrientSums.getOrDefault(category, 0.0F) + value));
        }
        String maxCategory = null;
        double maxSum = 0.0D;
        for (Map.Entry<String, Float> entry : nutrientSums.entrySet()) {
            if (entry.getValue() > maxSum) {
                maxSum = entry.getValue();
                maxCategory = entry.getKey();
            }
        }
        if (maxCategory != null) {
            List<NutrientCategoryRecipe> recipes = getNutrientCategoryByName(level, maxCategory);
            if (!recipes.isEmpty() && recipes.getFirst() != null) {
                return recipes.getFirst().getColorWithAlpha();
            }
        }
        return 0xCC3F76E4;
    }
}
