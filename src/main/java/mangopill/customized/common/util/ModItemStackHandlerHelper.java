package mangopill.customized.common.util;

import mangopill.customized.common.tag.ModTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.*;
import java.util.stream.Collectors;

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
        return Math.min(16, findMinStack(stackList).getCount());
    }

    public static boolean hasInput(ItemStackHandler itemStackHandler, int endIndex){
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
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

    public static void getOutputItem(ItemStack itemStackInHand, Player player, ItemStackHandler itemStackHandler, int slot) {
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(slot);
        int itemStackInHandCount = itemStackInHand.getCount();
        int stackInSlotCount = stackInSlot.getCount();
        int min = Math.min(stackInSlotCount, itemStackInHandCount);
        ItemStack newStack = stackInSlot.split(min).copy();
        if (!player.getInventory().add(newStack)) {
            player.drop(newStack, false);
        }
        itemStackInHand.shrink(min);
    }

    public static void getRemainingItemSpawn(Player player, List<ItemStack> stackList) {
        List<ItemStack> remainingList = stackList.stream().flatMap(itemStack -> {
            ItemStack remainingItem = itemStack.getCraftingRemainingItem();
            int quantity = itemStack.getCount();
            remainingItem.setCount(quantity);
            return java.util.stream.Stream.of(remainingItem);
        }).toList();
        remainingList.forEach(craftingRemainingItem -> {
            if (!player.getInventory().add(craftingRemainingItem)) {
                player.drop(craftingRemainingItem, false);
            }
        });
    }
}
