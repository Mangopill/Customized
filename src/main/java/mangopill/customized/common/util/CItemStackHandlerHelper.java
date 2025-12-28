package mangopill.customized.common.util;

import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.component.IItemMatchMode;
import net.minecraft.core.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;

import static mangopill.customized.common.util.InteractUtil.*;
import static mangopill.customized.common.util.component.CItemMatchMode.*;

public final class CItemStackHandlerHelper {
    private CItemStackHandlerHelper() {}

    /**
     * @see #fillInItem(ItemStackHandler, ItemStack, int, int, IItemMatchMode)
     */
    public static void fillInItem(ItemStackHandler itemStackHandler, ItemStack itemStack, int startIndex, int endIndex) {
        fillInItem(itemStackHandler, itemStack, startIndex, endIndex, SAME_ITEM_SAME_COMPONENTS);
    }

    /**
     * Fills items into the specified range of slots in the ItemStackHandler.
     * <p>
     * This method attempts to insert the given ItemStack into slots within the specified range.
     * It will first try to merge with existing stacks that match the specified match mode.
     * If no matching stack is found or there's remaining count, it will fill into empty slots.
     * The insertion continues until the ItemStack is fully inserted or all slots are processed.
     * @param itemStackHandler The ItemStackHandler to insert items into
     * @param itemStack The ItemStack to insert (will be modified during insertion)
     * @param startIndex The starting slot index (inclusive)
     * @param endIndex The ending slot index (exclusive)
     * @param matchMode The matching mode to determine how items should be compared
     */
    public  static void fillInItem(ItemStackHandler itemStackHandler, ItemStack itemStack,
                                   int startIndex, int endIndex, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        for (int i = startIndex; i < endIndex; i++) {
            ItemStack newItemStackInHand = itemStack.copy();
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(i);
            int slotLimit = Math.min(itemStackHandler.getSlotLimit(i), itemStack.getMaxStackSize());
            int itemStackInHandCount = itemStack.getCount();
            int stackInSlotCount = stackInSlot.getCount();
            if (stackInSlot.isEmpty()) {
                if (slotLimit >= itemStackInHandCount){
                    itemStackHandler.setStackInSlot(i, newItemStackInHand);
                    itemStack.copyAndClear();
                    break;
                }else {
                    itemStackHandler.setStackInSlot(i, newItemStackInHand.split(slotLimit));
                }
            }else {
                if (!simpleTest(stackInSlot, itemStack, matchMode)) continue;
                if (slotLimit >= stackInSlotCount + itemStackInHandCount){
                    stackInSlot.grow(itemStackInHandCount);
                    itemStack.copyAndClear();
                    break;
                }else {
                    shrinkItemStack(itemStack, slotLimit - stackInSlotCount);
                    stackInSlot.grow(slotLimit - stackInSlotCount);
                }
            }
        }
    }

    /**
     * @see #insertItem(ItemStack, ItemStackHandler, int, int, int, int, Ingredient, IItemMatchMode)
     */
    public static void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler,
                                  int ingredientInput, int seasoningInput, int spiceInput, int outPut,
                                  @Nullable Ingredient containerItem) {
        insertItem(itemStackInHand, itemStackHandler, ingredientInput, seasoningInput, spiceInput, outPut, containerItem, SAME_ITEM_SAME_COMPONENTS);
    }

    /**
     * Inserts an item into the appropriate slot category based on item type and tags.
     * <p>
     * This method categorizes items and inserts them into different sections of the handler:
     * - Seasoning items (ModTag.SEASONING) go to seasoning input slots
     * - Famous spice items (ModTag.FAMOUS_SPICE) go to spice input slots
     * - Container items (if specified) or AbstractPlateItem instances go to output slots
     * - All other items go to ingredient input slots
     * @param itemStackInHand The ItemStack to insert
     * @param itemStackHandler The ItemStackHandler to insert into
     * @param ingredientInput The number of ingredient input slots
     * @param seasoningInput The number of seasoning input slots
     * @param spiceInput The number of spice input slots
     * @param outPut The number of output slots
     * @param containerItem Optional ingredient representing allowed container items
     * @param matchMode The matching mode for item comparison
     */
    public static void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler,
                                  int ingredientInput, int seasoningInput, int spiceInput, int outPut,
                                  @Nullable Ingredient containerItem, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        if (itemStackInHand.is(ModTag.SEASONING)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput, ingredientInput + seasoningInput, matchMode);
            return;
        }
        if (itemStackInHand.is(ModTag.FAMOUS_SPICE)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput + seasoningInput, ingredientInput + seasoningInput + spiceInput, matchMode);
            return;
        }
        if (containerItem != null && ((containsSameItem(List.of(containerItem.getItems()), itemStackInHand, matchMode)) || itemStackInHand.getItem() instanceof AbstractPlateItem)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput + seasoningInput + spiceInput, ingredientInput + seasoningInput + spiceInput + outPut, matchMode);
            return;
        }
        fillInItem(itemStackHandler, itemStackInHand, 0, ingredientInput, matchMode);
    }

    /**
     * @see #getItemStackListInSlot(IItemHandler, int, int, boolean)
     */
    public static List<ItemStack> getItemStackListInSlot(IItemHandler itemStackHandler, int startIndex, int endIndex) {
        return getItemStackListInSlot(itemStackHandler, startIndex, endIndex, false);
    }

    /**
     * Retrieves a list of ItemStacks from the specified slot range in an ItemStackHandler.
     * <p>
     * This method iterates through all slots in the given range and collects ItemStacks.
     * @param itemStackHandler The ItemStackHandler to retrieve items from
     * @param startIndex The starting slot index (inclusive)
     * @param endIndex The ending slot index (exclusive)
     * @param includeEmpty Whether to include empty ItemStacks in the returned list
     * @return A list containing ItemStacks within the specified slot range
     */
    public static List<ItemStack> getItemStackListInSlot(IItemHandler itemStackHandler, int startIndex, int endIndex,
                                                         boolean includeEmpty){
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = startIndex; i < endIndex; ++i) {
            if (!includeEmpty && itemStackHandler.getStackInSlot(i).isEmpty()) continue;
            stackList.add(itemStackHandler.getStackInSlot(i));
        }
        return stackList;
    }

    /**
     * Creates a copy of the specified ItemStackHandler with optionally copied ItemStacks.
     * <p>
     * This method creates a new ItemStackHandler instance that replicates the contents
     * of the source handler. The behavior depends on the {@code copyItemStack} parameter:
     * <ul>
     *   <li>If {@code copyItemStack} is true, each ItemStack in the source handler
     *       is deeply copied using {@link ItemStack#copy()}, resulting in completely
     *       independent ItemStack instances.</li>
     *   <li>If {@code copyItemStack} is false, the original ItemStack references
     *       are reused, meaning changes to items in the new handler may affect
     *       the original handler's items and vice versa.</li>
     * </ul>
     * @param itemStackHandler The source IItemHandler to copy items from
     * @param copyItemStack    If true, creates deep copies of each ItemStack;
     *                         if false, reuses the original ItemStack references
     * @return A new ItemStackHandler containing the items from the source handler,
     *         with ItemStacks either copied or referenced based on the parameter
     */
    public static ItemStackHandler copyItemStackHandler(IItemHandler itemStackHandler, boolean copyItemStack) {
        List<ItemStack> stackList = getItemStackListInSlot(itemStackHandler, 0, itemStackHandler.getSlots(), true);
        List<ItemStack> sourceList = copyItemStack ? stackList.stream().map(ItemStack::copy).toList() : stackList;
        NonNullList<ItemStack> mutableList = NonNullList.create();
        mutableList.addAll(sourceList);
        return new ItemStackHandler(mutableList);
    }

    /**
     * Reduces the count of ItemStacks in the handler by a calculated division ratio.
     * <p>
     * This method calculates the reduction count for each slot based on the initial stack count
     * divided by the total consumption count. The reduction is applied proportionally across all slots.
     * <p>
     * For each slot:
     * - Calculates shrink count as: initial stack count / total consumption count (rounded)
     * - If calculated shrink count >= current stack count, clears the stack completely
     * - Otherwise, reduces the current stack by the calculated amount
     * @param itemStackHandler The ItemStackHandler containing current stacks to be reduced
     * @param initialItemStackHandler The ItemStackHandler containing initial stack counts for calculation
     * @param consumptionCountTotal The divisor used to calculate the reduction amount for each stack
     */
    public static void reduceItemStackCountByDivision(IItemHandler itemStackHandler, ItemStackHandler initialItemStackHandler,
                                                      int consumptionCountTotal) {
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            ItemStack initialStack = initialItemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                int shrinkCount = Math.round((float) initialStack.getCount() / consumptionCountTotal);
                if (shrinkCount >= stack.getCount()) {
                    stack.copyAndClear();
                    continue;
                }
                stack.shrink(shrinkCount);
            }
        }
    }

    /**
     * Clears all slots in the ItemStackHandler by setting each slot to empty.
     * <p>
     * This method iterates through every slot in the ItemStackHandler and clears
     * the ItemStack in each slot, effectively removing all items from the handler.
     */
    public static void clearAllSlot(IItemHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            itemStackHandler.getStackInSlot(i).copyAndClear();
        }
    }

    /**
     * Finds the ItemStack with the smallest count from a list of ItemStacks.
     * <p>
     * Note that this method compares the count of each individual ItemStack, without merging counts of the same item {@link #getMinTotalItemCount(Collection)}.
     * <p>
     * This method iterates through the list and returns the ItemStack with the lowest count value.
     * If the list is empty, returns an empty ItemStack.
     * @param stackList The list of ItemStacks to search through
     * @return The ItemStack with the smallest count, or ItemStack. EMPTY if the list is empty
     */
    public static ItemStack findMinStack(List<ItemStack> stackList) {
        if (stackList.isEmpty()) return ItemStack.EMPTY;
        ItemStack minStack = stackList.getFirst();
        for (ItemStack stack : stackList) {
            if (stack.getCount() >= minStack.getCount()) continue;
            minStack = stack;
        }
        return minStack;
    }

    /**
     * Calculates the minimum total count of items after merging stacks of the same item.
     * The difference from {@link #findMinStack(List)} is:
     * this method merges counts of the same item, while {@link #findMinStack(List)} compares the individual count of each ItemStack.
     * <p>
     * This method first groups the ItemStacks by item, summing up the counts for each item,
     * then returns the smallest total count among these items.
     * If the collection is empty, returns 0.
     * @param stackList The collection of ItemStacks to process
     * @return The minimum total count of any item after merging, or 0 if the collection is empty
     */
    public static int getMinTotalItemCount(Collection<ItemStack> stackList) {
        if (stackList.isEmpty()) return 0;
        Map<Item, Integer> itemTotalCountMap = new HashMap<>();
        for (ItemStack stack : stackList) {
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            int count = stack.getCount();
            itemTotalCountMap.put(item, itemTotalCountMap.getOrDefault(item, 0) + count);
        }
        return itemTotalCountMap.values().stream().min(Integer::compareTo).orElse(0);
    }

    /**
     * Calculates the consumption count based on the smallest stack in the list.
     * @param stackList The list of ItemStacks to analyze
     * @return The minimum value between 16 and the count of the smallest stack
     */
    public static int getConsumptionCount(List<ItemStack> stackList) {
        return Math.min(16, findMinStack(stackList).getCount());
    }

    /**
     * Checks if the ItemStackHandler has any non-empty slots within the specified range.
     * @param itemStackHandler The ItemStackHandler to check
     * @param endIndex The end index of the range to check (exclusive)
     * @return true if any slot in the range [0, endIndex) is not empty, false otherwise
     */
    public static boolean hasInput(IItemHandler itemStackHandler, int endIndex){
        return getNonEmptySlotCount(itemStackHandler, endIndex) > 0;
    }

    /**
     * Counts the number of non-empty slots within the specified range.
     * @param itemStackHandler The ItemStackHandler to count from
     * @param endIndex The end index of the range to count (exclusive)
     * @return The number of non-empty slots in the range [0, endIndex)
     */
    public static int getNonEmptySlotCount(IItemHandler itemStackHandler, int endIndex) {
        int count = 0;
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * @see #containsSameItem(Collection, ItemStack, IItemMatchMode)
     */
    public static boolean containsSameItem(Collection<ItemStack> itemStackList, ItemStack targetStack) {
        return containsSameItem(itemStackList, targetStack, SAME_ITEM);
    }

    /**
     * @see #containsSameItem(Collection, Collection, IItemMatchMode)
     */
    public static boolean containsSameItem(Collection<ItemStack> itemStackList, ItemStack targetStack, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return containsSameItem(itemStackList, List.of(targetStack), matchMode);
    }

    /**
     * Checks if the source collection contains all items from the target collection according to the specified match mode.
     * <p>
     * This method iterates through each item in the target collection and searches for a matching item in the source collection.
     * Returns true only when every item in the target collection has a corresponding match in the source collection.
     * @param itemStackList The source collection of ItemStacks to search within
     * @param targetStackList The target collection of ItemStacks to match against
     * @param matchMode The match mode to use for comparison
     * @return true if the source collection contains all items from the target collection (according to the match mode), false otherwise
     */
    public static boolean containsSameItem(Collection<ItemStack> itemStackList, Collection<ItemStack> targetStackList, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return targetStackList.stream().allMatch(target -> itemStackList.stream().anyMatch(stack -> simpleTest(stack, target, matchMode)));
    }

    /**
     * @see #test(ItemStack, ItemStack, BiPredicate)
     */
    public static boolean simpleTest(ItemStack stack, Item targetItem, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return test(stack, targetItem.getDefaultInstance(), matchMode.getComparator());
    }

    /**
     * @see #test(ItemStack, ItemStack, BiPredicate)
     */
    public static boolean simpleTest(ItemStack stack, ItemStack targetStack, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return test(stack, targetStack, matchMode.getComparator());
    }

    /**
     * Tests two ItemStacks using a custom BiPredicate comparator.
     * <p>
     * This method serves as a generic test utility that delegates the comparison logic
     * to the provided BiPredicate. It can be used for various comparison scenarios where
     * custom matching logic is required beyond the standard ItemMatchMode options.
     * @param stack The first ItemStack to compare
     * @param targetStack The second ItemStack to compare against
     * @param comparator A BiPredicate that defines the comparison logic between two ItemStacks
     * @return The result of the BiPredicate test applied to the two ItemStacks
     */
    public static boolean test(ItemStack stack, ItemStack targetStack, BiPredicate<ItemStack, ItemStack> comparator) {
        return comparator.test(stack, targetStack);
    }

    /**
     * Gets the top two items by total count from a list of ItemStacks.
     * <p>
     * This method aggregates counts by item type and returns the two items with the highest total counts.
     * @param itemStackList The list of ItemStacks to analyze
     * @return A list containing the top two items by count, represented as new ItemStacks with aggregated counts
     */
    public static List<ItemStack> getTopTwoItemsByCount(Collection<ItemStack> itemStackList) {
        Map<Item, Integer> itemCountMap = new HashMap<>();
        for (ItemStack itemStack : itemStackList) {
            Item item = itemStack.getItem();
            int count = itemStack.getCount();
            itemCountMap.put(item, itemCountMap.getOrDefault(item, 0) + count);
        }
        return itemCountMap.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .limit(2).map(entry -> new ItemStack(entry.getKey(), entry.getValue())).toList();
    }

    /**
     * Calculates the total count of all items in the list.
     * @param stackList The list of ItemStacks to sum
     * @return The sum of counts from all non-empty ItemStacks in the list, or 0 if the list is empty
     */
    public static int getTotalItemCount(Collection<ItemStack> stackList) {
        return stackList.isEmpty() ? 0 : stackList.stream().filter(stack -> !stack.isEmpty()).mapToInt(ItemStack::getCount).sum();
    }

    /**
     * @see #getTotalCountOf(Collection, Collection, IItemMatchMode)
     */
    public static int getTotalCountOf(Collection<ItemStack> itemStackList, Item target, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return getTotalCountOf(itemStackList, List.of(target.getDefaultInstance()), matchMode);
    }

    /**
     * @see #getTotalCountOf(Collection, Collection, IItemMatchMode)
     */
    public static int getTotalCountOf(Collection<ItemStack> itemStackList, ItemStack targetStack, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return getTotalCountOf(itemStackList, List.of(targetStack), matchMode);
    }

    /**
     * @see #getTotalCountOf(Collection, Collection, IItemMatchMode)
     */
    public static int getTotalCountOf(Collection<ItemStack> itemStackList, Ingredient ingredient, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return getTotalCountOf(itemStackList, List.of(ingredient.getItems()), matchMode);
    }

    /**
     * Calculates the total count of items that match any of the target items in a collection.
     * <p>
     * This method is useful when you need to count multiple types of items together.
     * For example, counting all types of wood logs or all types of dyes.
     * @param itemStackList The collection of ItemStacks to search
     * @param targetStacks The collection of ItemStacks to match against
     * @param matchMode The match mode to use for comparison
     * @return The total count of matching items
     */
    public static int getTotalCountOf(Collection<ItemStack> itemStackList, Collection<ItemStack> targetStacks, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        int totalCount = 0;
        for (ItemStack stack : itemStackList) {
            for (ItemStack targetStack : targetStacks) {
                if (!simpleTest(stack, targetStack, matchMode)) continue;
                totalCount += stack.getCount();
                break;
            }
        }
        return totalCount;
    }

    /**
     * Spawns item entities based on the usingConvertsTo or craftingRemainingItem properties of the input stacks.
     * <p>
     * For each ItemStack in the list, this method attempts to get either:
     * - The usingConvertsTo item from food properties, or
     * - The crafting remaining item
     * Then spawns item entities for the resulting items at the specified position.
     * @param level The level to spawn entities in
     * @param stackList The list of ItemStacks to process
     * @param state The block state at the spawn position (can be used for direction calculation)
     * @param pos The position to spawn entities at
     */
    public static void spawnUsingConvertsTo(Level level, Collection<ItemStack> stackList, BlockState state, BlockPos pos) {
        List<ItemStack> spawnList = stackList.stream().map(itemStack -> {
            Optional<ItemStack> optionalItem = Optional.ofNullable(itemStack.getFoodProperties(null))
                    .flatMap(FoodProperties::usingConvertsTo);
            ItemStack finalItem = optionalItem.orElseGet(itemStack::getCraftingRemainingItem);
            finalItem.setCount(itemStack.getCount());
            return finalItem;
        }).toList();
        spawnItemEntityList(level, spawnList, state, pos);
    }

    /**
     * @see #spawnItemEntityList(Level, Collection, BlockState, Vec3i, UUID)
     */
    public static void spawnItemEntityList(Level level, Collection<ItemStack> stackList, @Nullable BlockState state, Vec3i pos) {
        spawnItemEntityList(level, stackList, state, pos, null);
    }

    /**
     * @see #spawnItemEntity(Level, ItemStack, BlockState, Vec3i, UUID)
     */
    public static void spawnItemEntityList(Level level, Collection<ItemStack> stackList, @Nullable BlockState state, Vec3i pos, @Nullable UUID uuid) {
        stackList.forEach(stack -> spawnItemEntity(level, stack, state, pos, uuid));
    }

    /**
     * @see #spawnItemEntity(Level, ItemStack, BlockState, Vec3i, UUID)
     */
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3i pos) {
        spawnItemEntity(level, stack, state, pos, null);
    }

    /**
     * Spawns an item entity in the world with directional velocity.
     * <p>
     * The spawn position is adjusted based on the block's facing direction if available.
     * The item entity is given an upward and outward velocity based on the direction.
     * @param level The level to spawn the entity in
     * @param stack The ItemStack to spawn
     * @param state The block state for direction calculation (can be null)
     * @param pos The base position to spawn at
     * @param uuid Collectible entity
     */
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3i pos, @Nullable UUID uuid) {
        if (stack.isEmpty()) return;
        Direction direction = Direction.UP;
        if (state != null) {
            direction = state.hasProperty(BlockStateProperties.FACING)
                    ? state.getValue(BlockStateProperties.FACING)
                    : state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    ? state.getValue(BlockStateProperties.HORIZONTAL_FACING)
                    : Direction.UP;
        }
        double x = pos.getX() + 0.5D + (direction.getStepX() * 0.25D);
        double y = pos.getY() + 1.0D;
        double z = pos.getZ() + 0.5D + (direction.getStepZ() * 0.25D);
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.copy());
        itemEntity.setTarget(uuid);
        itemEntity.setDeltaMovement(direction.getStepX() * -0.1D, 0.45D, direction.getStepZ() * -0.1D);
        level.addFreshEntity(itemEntity);
        stack.copyAndClear();
    }

    /**
     * Shuffles the ItemStacks within a specified range of slots using Fisher-Yates algorithm.
     * <p>
     * This method randomly rearranges the ItemStacks between startIndex (inclusive) and endIndex (exclusive).
     * If the range is invalid or only one slot, no operation is performed.
     * @param itemStackHandler The ItemStackHandler to shuffle
     * @param startIndex The starting index of the range to shuffle (inclusive)
     * @param endIndex The ending index of the range to shuffle (exclusive)
     */
    public static void shuffleItemStackHandlerInRange(ItemStackHandler itemStackHandler, int startIndex, int endIndex) {
        int slotCount = itemStackHandler.getSlots();
        if (startIndex < 0 || endIndex > slotCount || startIndex >= endIndex) return;
        int rangeSize = endIndex - startIndex;
        if (rangeSize == 1) return;
        Random random = new Random();
        for (int i = endIndex - 1; i > startIndex; i--) {
            int j = startIndex + random.nextInt(i - startIndex + 1);
            ItemStack temp = itemStackHandler.getStackInSlot(i).copy();
            itemStackHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(j).copy());
            itemStackHandler.setStackInSlot(j, temp);
        }
    }

    /**
     * @see #shrinkMatchingItem(ItemStackHandler, ItemStack, int, IItemMatchMode)
     */
    public static int shrinkMatchingItemList(ItemStackHandler itemStackHandler, Collection<ItemStack> targetStackList, int shrinkCount,
                                             IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return targetStackList.stream().mapToInt(target -> shrinkMatchingItem(itemStackHandler, target, shrinkCount, matchMode)).sum();
    }

    /**
     * @see #shrinkMatchingItem(ItemStackHandler, ItemStack, int, IItemMatchMode)
     */
    public static int shrinkMatchingItem(ItemStackHandler itemStackHandler, @Nullable ItemStack targetStack, int shrinkCount) {
        return shrinkMatchingItem(itemStackHandler, targetStack, shrinkCount, SAME_ITEM);
    }

    /**
     * @see #shrinkMatchingItemInRange(IItemHandler, ItemStack, int, int, int, IItemMatchMode)
     */
    public static int shrinkMatchingItem(ItemStackHandler itemStackHandler, @Nullable ItemStack targetStack, int shrinkCount,
                                         IItemMatchMode<ItemStack, ItemStack> matchMode) {
        return shrinkMatchingItemInRange(itemStackHandler, targetStack, shrinkCount, 0, itemStackHandler.getSlots(), matchMode);
    }

    /**
     * Reduces the count of matching items within a specific slot range.
     * <p>
     * This method searches through the specified range of slots and reduces the count
     * of items that match the target stack according to the specified match mode.
     * The reduction continues until the requested shrink count is met, the range
     * is exhausted, or all matching items are processed.
     * @param itemStackHandler The IItemHandler containing items to shrink
     * @param targetStack The ItemStack to match against (null matches any non-empty stack)
     * @param shrinkCount The number of items to remove
     * @param startIndex The starting slot index (inclusive)
     * @param endIndex The ending slot index (exclusive)
     * @param matchMode The match mode for item comparison
     * @return The actual number of items removed
     */
    public static int shrinkMatchingItemInRange(IItemHandler itemStackHandler, @Nullable ItemStack targetStack,
                                                int shrinkCount, int startIndex, int endIndex, IItemMatchMode<ItemStack, ItemStack> matchMode) {
        int remaining = shrinkCount;
        for (int i = startIndex; i < endIndex && remaining > 0; i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            boolean matches = targetStack == null ? !stack.isEmpty() : (!stack.isEmpty() && simpleTest(stack, targetStack, matchMode));
            if (matches) {
                int remove = Math.min(stack.getCount(), remaining);
                shrinkItemStack(stack, remove);
                remaining -= remove;
            }
        }
        return shrinkCount - remaining;
    }
}
