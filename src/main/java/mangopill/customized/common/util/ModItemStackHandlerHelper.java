package mangopill.customized.common.util;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.value.NutrientBuff.*;

public final class ModItemStackHandlerHelper {
    private ModItemStackHandlerHelper() {
    }

    public static void fillInItem(ItemStackHandler itemStackHandler, ItemStack itemStackInHand, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            ItemStack newItemStackInHand = itemStackInHand.copy();
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(i);
            int slotLimit = Math.min(itemStackHandler.getSlotLimit(i), itemStackInHand.getMaxStackSize());
            int itemStackInHandCount = itemStackInHand.getCount();
            int stackInSlotCount = stackInSlot.getCount();
            if (stackInSlot.isEmpty()) {
                if (slotLimit >= itemStackInHandCount){
                    itemStackHandler.setStackInSlot(i, newItemStackInHand);
                    itemStackInHand.copyAndClear();
                    break;
                }else {
                    itemStackHandler.setStackInSlot(i, newItemStackInHand.split(slotLimit));
                }
            }else {
                if (!ItemStack.isSameItem(stackInSlot, itemStackInHand)){
                    continue;
                }
                if (slotLimit >= stackInSlotCount + itemStackInHandCount){
                    stackInSlot.grow(itemStackInHandCount);
                    itemStackInHand.copyAndClear();
                    break;
                }else {
                    itemStackInHand.shrink(slotLimit - stackInSlotCount);
                    stackInSlot.grow(slotLimit - stackInSlotCount);
                }
            }
        }
    }

    public static void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler, int ingredientInput, int seasoningInput, int spiceInput) {
        if (itemStackInHand.is(ModTag.SEASONING)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput ,ingredientInput + seasoningInput);
            return;
        }
        if (itemStackInHand.is(ModTag.FAMOUS_SPICE)) {
            fillInItem(itemStackHandler, itemStackInHand,ingredientInput + seasoningInput ,ingredientInput + seasoningInput + spiceInput);
            return;
        }
        fillInItem(itemStackHandler, itemStackInHand,0, ingredientInput);
    }

    public static List<ItemStack> getItemStackListInSlot(ItemStackHandler itemStackHandler, int startIndex, int endIndex){
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = startIndex; i < endIndex; ++i) {
            if (itemStackHandler.getStackInSlot(i).isEmpty()) {
                continue;
            }
            stackList.add(itemStackHandler.getStackInSlot(i));
        }
        return stackList;
    }

    public static void reduceItemStackCountByDivision(ItemStackHandler itemStackHandler, int consumptionCount) {
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                int shrinkCount = Math.round((float) stack.getCount() / consumptionCount);
                if (shrinkCount >= stack.getCount()) {
                    stack.copyAndClear();
                    continue;
                }
                stack.shrink(shrinkCount);
            }
        }
    }

    public static void clearAllSlot(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            itemStackHandler.getStackInSlot(i).copyAndClear();
        }
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

    public static ItemStack findMinStack(List<ItemStack> stackList) {
        if (stackList == null || stackList.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack minStack = stackList.getFirst();
        for (ItemStack stack : stackList) {
            if (stack.getCount() < minStack.getCount()) {
                minStack = stack;
            }
        }
        return minStack;
    }

    public static int getConsumptionCount(List<ItemStack> stackList) {
        int consumptionCount = findMinStack(stackList).getCount() / 4;
        if (findMinStack(stackList).getCount() % 4 > 0) {
            consumptionCount++;
        }
        return consumptionCount;
    }

    public static List<FoodProperties.PossibleEffect> getCustomizedFoodEffectList(Map<NutrientCategory, Float> nutrientTotal){
        List<FoodProperties.PossibleEffect> foodEffect = new ArrayList<>();
        List<Float> valueList = new ArrayList<>(nutrientTotal.values());
        for (NutrientCategory category : nutrientTotal.keySet()){
            float nutrientValue = nutrientTotal.get(category);
            if (nutrientValue <= 0.0F){
                continue;
            }
            if (NORMAL_BUFF.get()) {
                addBuffToList(nutrientValue, foodEffect, getNormalBuff(category));
            }
            if (POWERFUL_BUFF.get()) {
                addBuffToList(nutrientValue, foodEffect, getPowerfulBuff(category));
            }
        }
        return foodEffect;
    }

    public static void addBuffToList(float nutrientValue, List<FoodProperties.PossibleEffect> foodEffect, NutrientBuff nutrientBuff) {
            if (nutrientBuff == null){
                return;
            }
            int duration = Math.round( nutrientValue * 10 * (float) nutrientBuff.getDuration());
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

    public static boolean hasInput(ItemStackHandler itemStackHandler, int endIndex){
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
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

    public static List<ItemStack> getTopTwoItemsByCount(List<ItemStack> itemStackList) {
        Map<Item, Integer> itemCountMap = new HashMap<>();
        for (ItemStack itemStack : itemStackList) {
            if (itemStack != null) {
                Item item = itemStack.getItem();
                int count = itemStack.getCount();
                itemCountMap.put(item, itemCountMap.getOrDefault(item, 0) + count);
            }
        }
        return itemCountMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .limit(2)
                .map(entry -> new ItemStack(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
