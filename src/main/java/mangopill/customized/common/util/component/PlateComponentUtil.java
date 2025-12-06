package mangopill.customized.common.util.component;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.UUID;

public final class PlateComponentUtil {
    private PlateComponentUtil() {
    }
    public static FoodProperties getFoodProperty(ItemStack stack) {
        return stack.getOrDefault(DataComponents.FOOD, FoodValue.EMPTY);
    }

    public static ItemStackHandler getItemStackHandler(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL).itemStackHandler();
    }

    public static ItemStackHandler getInitialItemStackHandler(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL).itemStackHandler();
    }

    public static int getConsumptionCount(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT, ConsumptionCountRecord.NULL).consumptionCount();
    }

    public static int getConsumptionCountTotal(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, ConsumptionCountTotalRecord.NULL).consumptionCountTotal();
    }

    public static UUID getLastInteractPlayerId(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.UUID, UUIDRecord.NULL).uuid();
    }

    public static Boolean getAdvancementHasProgress(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, false);
    }

    public static void setFoodProperty(ItemStack stack, FoodProperties foodProperties) {
        stack.set(DataComponents.FOOD, foodProperties);
    }

    public static void setItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.set(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    public static void setInitialItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.set(CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    public static void setConsumptionCount(ItemStack stack, int consumptionCount) {
        stack.set(CDataComponentRegistry.CONSUMPTION_COUNT, new ConsumptionCountRecord(consumptionCount));
    }

    public static void setConsumptionCountTotal(ItemStack stack, int consumptionCountTotal) {
        stack.set(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, new ConsumptionCountTotalRecord(consumptionCountTotal));
    }

    public static void setLastInteractPlayerId(ItemStack stack, UUID lastInteractPlayerId) {
        stack.set(CDataComponentRegistry.UUID, new UUIDRecord(lastInteractPlayerId));
    }

    public static void setAdvancementHasProgress(ItemStack stack, Boolean hasProgress) {
       stack.set(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, hasProgress);
    }

    public static void updateAll(ItemStack stack, ItemStackHandler itemStackHandler,
                                 ItemStackHandler initialItemStackHandler, FoodProperties foodProperty,
                                 int consumptionCount, int consumptionCountTotal,
                                 UUID lastInteractPlayerId, Boolean hasProgress) {
        setItemStackHandler(stack, itemStackHandler);
        setInitialItemStackHandler(stack, initialItemStackHandler);
        setFoodProperty(stack, foodProperty);
        setConsumptionCount(stack, consumptionCount);
        setConsumptionCountTotal(stack, consumptionCountTotal);
        setLastInteractPlayerId(stack, lastInteractPlayerId);
        setAdvancementHasProgress(stack, hasProgress);
    }
}
