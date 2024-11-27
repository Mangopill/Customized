package mangopill.customized.common.util;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.registry.ModDataComponentRegistry;
import mangopill.customized.common.util.record.ConsumptionCountRecord;
import mangopill.customized.common.util.record.ConsumptionCountTotalRecord;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class PlateComponentUtil {
    private PlateComponentUtil() {
    }
    public static FoodProperties getFoodProperty(ItemStack stack) {
        return stack.getOrDefault(DataComponents.FOOD, FoodValue.NULL);
    }

    public static ItemStackHandler getItemStackHandler(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL).itemStackHandler();
    }

    public static int getConsumptionCount(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistry.CONSUMPTION_COUNT, ConsumptionCountRecord.NULL).consumptionCount();
    }

    public static int getConsumptionCountTotal(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, ConsumptionCountTotalRecord.NULL).consumptionCountTotal();
    }

    public static void setFoodProperty(ItemStack stack, FoodProperties foodProperties) {
        stack.set(DataComponents.FOOD, foodProperties);
    }

    public static void setItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.set(ModDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    public static void setConsumptionCount(ItemStack stack, int consumptionCount) {
        stack.set(ModDataComponentRegistry.CONSUMPTION_COUNT, new ConsumptionCountRecord(consumptionCount));
    }

    public static void setConsumptionCountTotal(ItemStack stack, int consumptionCountTotal) {
        stack.set(ModDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, new ConsumptionCountTotalRecord(consumptionCountTotal));
    }

    public static void updateAll(ItemStack stack, ItemStackHandler itemStackHandler, FoodProperties foodProperty, int consumptionCount, int consumptionCountTotal) {
        setItemStackHandler(stack, itemStackHandler);
        setFoodProperty(stack, foodProperty);
        setConsumptionCount(stack, consumptionCount);
        setConsumptionCountTotal(stack, consumptionCountTotal);
    }
}
