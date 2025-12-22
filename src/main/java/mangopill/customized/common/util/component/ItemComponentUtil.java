package mangopill.customized.common.util.component;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.UUID;

public final class ItemComponentUtil {
    private ItemComponentUtil() {
    }

    public static FoodProperties getFoodProperty(MutableDataComponentHolder holder) {
        return holder.getOrDefault(DataComponents.FOOD, FoodValue.EMPTY);
    }

    public static ItemStackHandler getItemStackHandler(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.EMPTY).itemStackHandler();
    }

    public static ItemStackHandler getInitialItemStackHandler(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, ItemStackHandlerRecord.EMPTY).itemStackHandler();
    }

    public static int getConsumptionCount(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT, ConsumptionCountRecord.NULL).consumptionCount();
    }

    public static int getConsumptionCountTotal(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, ConsumptionCountTotalRecord.NULL).consumptionCountTotal();
    }

    public static UUID getLastInteractPlayerId(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.UUID, UUIDRecord.EMPTY).uuid();
    }

    public static Boolean getAdvancementHasProgress(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, false);
    }

    public static void setFoodProperty(MutableDataComponentHolder holder, FoodProperties foodProperties) {
        holder.set(DataComponents.FOOD, foodProperties);
    }

    public static void setItemStackHandler(MutableDataComponentHolder holder, ItemStackHandler itemStackHandler) {
        holder.set(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    public static void setInitialItemStackHandler(MutableDataComponentHolder holder, ItemStackHandler itemStackHandler) {
        holder.set(CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    public static void setConsumptionCount(MutableDataComponentHolder holder, int consumptionCount) {
        holder.set(CDataComponentRegistry.CONSUMPTION_COUNT, new ConsumptionCountRecord(consumptionCount));
    }

    public static void setConsumptionCountTotal(MutableDataComponentHolder holder, int consumptionCountTotal) {
        holder.set(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, new ConsumptionCountTotalRecord(consumptionCountTotal));
    }

    public static void setLastInteractPlayerId(MutableDataComponentHolder holder, UUID lastInteractPlayerId) {
        holder.set(CDataComponentRegistry.UUID, new UUIDRecord(lastInteractPlayerId));
    }

    public static void setAdvancementHasProgress(MutableDataComponentHolder holder, Boolean hasProgress) {
        holder.set(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, hasProgress);
    }

    public static void updatePlateAll(MutableDataComponentHolder holder, ItemStackHandler itemStackHandler,
                                      ItemStackHandler initialItemStackHandler, FoodProperties foodProperty,
                                      int consumptionCount, int consumptionCountTotal,
                                      UUID lastInteractPlayerId, Boolean hasProgress) {
        setItemStackHandler(holder, itemStackHandler);
        setInitialItemStackHandler(holder, initialItemStackHandler);
        setFoodProperty(holder, foodProperty);
        setConsumptionCount(holder, consumptionCount);
        setConsumptionCountTotal(holder, consumptionCountTotal);
        setLastInteractPlayerId(holder, lastInteractPlayerId);
        setAdvancementHasProgress(holder, hasProgress);
    }
}
