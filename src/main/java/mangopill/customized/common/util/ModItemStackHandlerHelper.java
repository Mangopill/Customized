package mangopill.customized.common.util;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static mangopill.customized.common.CustomizedConfig.NORMAL_BUFF;
import static mangopill.customized.common.util.category.NutrientCategory.COLD;
import static mangopill.customized.common.util.category.NutrientCategory.WARM;
import static mangopill.customized.common.util.value.NutrientBuff.ICED;
import static mangopill.customized.common.util.value.NutrientBuff.WARM_STOMACH;

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
                System.out.println(shrinkCount);
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
            @NotNull PropertyValue propertyValue = PropertyValueRecipe.getPropertyValue(stack, level);
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
                builder.saturationModifier(newSaturationValue.floatValue());
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
                builder.saturationModifier(newSaturationValue.floatValue());
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
        if (NORMAL_BUFF.get()){
            for (NutrientCategory category : nutrientTotal.keySet()){
                float nutrientValue = nutrientTotal.get(category);
                NutrientBuff nutrientBuff = getNormalBuff(category ,nutrientValue);
                if (nutrientBuff == null){
                    continue;
                }
                int duration = Math.round( nutrientValue * 10 * (float) nutrientBuff.getDuration());
                float probability = (float) (nutrientValue * 10 * nutrientBuff.getProbability());
                foodEffect.add(new FoodProperties.PossibleEffect(
                        () -> new MobEffectInstance(nutrientBuff.getEffect(), duration, 0), Math.min(probability, 1.0F)));
            }
        }
        return foodEffect;
    }

    public static @Nullable NutrientBuff getNormalBuff(NutrientCategory category, Float nutrientValue){
        if (nutrientValue <= 0.0F){
            return null;
        }
        if (category.name().equals(COLD.name())){
            return ICED;
        }
        if (category.name().equals(WARM.name())){
            return WARM_STOMACH;
        }
        return null;
    }

    public static boolean hasInput(ItemStackHandler itemStackHandler, int endIndex){
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
