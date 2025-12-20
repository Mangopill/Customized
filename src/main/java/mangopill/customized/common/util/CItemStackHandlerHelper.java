package mangopill.customized.common.util;

import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public final class CItemStackHandlerHelper {
    private CItemStackHandlerHelper() {
    }

    /**
     * Fills the specified ItemStack into the given slot range of the ItemStackHandler.
     * <p>
     * This method iterates through the specified slot range and attempts to place the ItemStack into appropriate slots. The processing logic includes:
     * - If the slot is empty and has sufficient capacity, places the entire ItemStack
     * - If the slot is empty but has insufficient capacity, places part of the items and continues processing the remainder
     * - If the slot already contains items that match the target items, attempts to merge the ItemStack
     * - If the slot contains different items, skips the slot and continues to the next one
     * @param itemStackHandler The target ItemStackHandler to be filled
     * @param itemStack The ItemStack to fill (will be modified)
     * @param startIndex The starting slot index to fill (inclusive)
     * @param endIndex The ending slot index to fill (exclusive)
     */
    public static void fillInItem(ItemStackHandler itemStackHandler, ItemStack itemStack, int startIndex, int endIndex) {
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
                if (!ItemStack.isSameItemSameComponents(stackInSlot, itemStack)) continue;
                if (slotLimit >= stackInSlotCount + itemStackInHandCount){
                    stackInSlot.grow(itemStackInHandCount);
                    itemStack.copyAndClear();
                    break;
                }else {
                    itemStack.shrink(slotLimit - stackInSlotCount);
                    stackInSlot.grow(slotLimit - stackInSlotCount);
                }
            }
        }
    }

    /**
     * Inserts an ItemStack into the appropriate section of the ItemStackHandler based on its type.
     * <p>
     * This method routes the ItemStack to different slot ranges depending on its characteristics:
     * - Seasoning items are inserted into the seasoning section
     * - Famous spice items are inserted into the spice section
     * - Container items or plate items are inserted into the output section
     * - All other items are inserted into the ingredient section
     * @param itemStackInHand The ItemStack to be inserted
     * @param itemStackHandler The ItemStackHandler to insert into
     * @param ingredientInput The starting index and size of the ingredient section
     * @param seasoningInput The size of the seasoning section
     * @param spiceInput The size of the spice section
     * @param outPut The size of the output section
     * @param containerItem The container ingredient, if any (can be null)
     */
    public static void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler, int ingredientInput, int seasoningInput, int spiceInput, int outPut, @Nullable Ingredient containerItem) {
        if (itemStackInHand.is(ModTag.SEASONING)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput ,ingredientInput + seasoningInput);
            return;
        }
        if (itemStackInHand.is(ModTag.FAMOUS_SPICE)) {
            fillInItem(itemStackHandler, itemStackInHand,ingredientInput + seasoningInput ,ingredientInput + seasoningInput + spiceInput);
            return;
        }
        if (containerItem != null && ((containsSameItem(List.of(containerItem.getItems()), itemStackInHand)) || itemStackInHand.getItem() instanceof AbstractPlateItem)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput + seasoningInput + spiceInput, ingredientInput + seasoningInput + spiceInput + outPut);
            return;
        }
        fillInItem(itemStackHandler, itemStackInHand,0, ingredientInput);
    }

    /**
     * Retrieves a list of non-empty ItemStacks from the specified slot range in an ItemStackHandler.
     * <p>
     * This method iterates through all slots in the given range and collects all non-empty ItemStacks.
     * Only slots that actually contain items will be included in the returned list; empty slots are skipped.
     * @param itemStackHandler The ItemStackHandler to retrieve items from
     * @param startIndex The starting slot index (inclusive)
     * @param endIndex The ending slot index (exclusive)
     * @return A list containing all non-empty ItemStacks within the specified slot range
     */
    public static List<ItemStack> getItemStackListInSlot(ItemStackHandler itemStackHandler, int startIndex, int endIndex){
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = startIndex; i < endIndex; ++i) {
            if (itemStackHandler.getStackInSlot(i).isEmpty()) continue;
            stackList.add(itemStackHandler.getStackInSlot(i));
        }
        return stackList;
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
    public static void reduceItemStackCountByDivision(ItemStackHandler itemStackHandler, ItemStackHandler initialItemStackHandler, int consumptionCountTotal) {
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
    public static void clearAllSlot(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            itemStackHandler.getStackInSlot(i).copyAndClear();
        }
    }

    /**
     * Finds the ItemStack with the smallest count from a list of ItemStacks.
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
            if (stack.getCount() < minStack.getCount()) {
                minStack = stack;
            }
        }
        return minStack;
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
    public static boolean hasInput(ItemStackHandler itemStackHandler, int endIndex){
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    /**
     * Counts the number of non-empty slots within the specified range.
     * @param itemStackHandler The ItemStackHandler to count from
     * @param endIndex The end index of the range to count (exclusive)
     * @return The number of non-empty slots in the range [0, endIndex)
     */
    public static int getNonEmptySlotCount(ItemStackHandler itemStackHandler, int endIndex) {
        int count = 0;
        for (int i = 0; i < endIndex; ++i) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if the target ItemStack matches any ItemStack in the list by item type and components.
     * @param itemStackList The list of ItemStacks to search through
     * @param targetStack The ItemStack to match against
     * @return true if any ItemStack in the list matches the target by item type and components, false otherwise
     */
    public static boolean containsSameItem(Collection<ItemStack> itemStackList, ItemStack targetStack) {
        if (itemStackList.isEmpty() && targetStack.isEmpty()) return true;
        for (ItemStack stack : itemStackList) {
            if (ItemStack.isSameItem(stack, targetStack)) return true;
        }
        return false;
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
            if (itemStack == null) continue;
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
        if (stackList.isEmpty()) return 0;
        return stackList.stream().filter(stack -> !stack.isEmpty()).mapToInt(ItemStack::getCount).sum();
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
        List<ItemStack> spawnList = stackList.stream().flatMap(itemStack -> {
            Optional<ItemStack> optionalItem = Optional.ofNullable(itemStack.getFoodProperties(null))
                    .flatMap(FoodProperties::usingConvertsTo);
            ItemStack finalItem = optionalItem.orElseGet(itemStack::getCraftingRemainingItem);
            finalItem.setCount(itemStack.getCount());
            return Stream.of(finalItem);
        }).toList();
        spawnList.forEach(craftingRemainingItem -> spawnItemEntity(level, craftingRemainingItem, state, pos));
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
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3 pos, @Nullable UUID uuid) {
        if (stack.isEmpty()) return;
        Direction direction = Direction.UP;
        if (state != null) {
            direction = state.hasProperty(BlockStateProperties.FACING)
                    ? state.getValue(BlockStateProperties.FACING)
                    : state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    ? state.getValue(BlockStateProperties.HORIZONTAL_FACING)
                    : Direction.UP;
        }
        double x = pos.x + 0.5D + (direction.getStepX() * 0.25D);
        double y = pos.y + 1.0D;
        double z = pos.z + 0.5D + (direction.getStepZ() * 0.25D);
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.copy());
        itemEntity.setTarget(uuid);
        itemEntity.setDeltaMovement(direction.getStepX() * -0.1D, 0.45D, direction.getStepZ() * -0.1D);
        level.addFreshEntity(itemEntity);
        stack.copyAndClear();
    }

    /**
     * Spawns an item entity in the world at the specified integer position.
     * <p>
     * This method converts the integer position to a vector position and delegates to
     * the main spawn method. See the main method for detailed implementation details.
     * @see #spawnItemEntity(Level, ItemStack, BlockState, Vec3, UUID)
     */
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3i pos, @Nullable UUID uuid) {
        spawnItemEntity(level, stack, state, Vec3.atLowerCornerOf(pos), uuid);
    }

    /**
     * Spawns an item entity in the world without entity targeting.
     * <p>
     * This method delegates to the main spawn method with a null UUID. See the main method
     * for detailed implementation details.
     * @see #spawnItemEntity(Level, ItemStack, BlockState, Vec3, UUID)
     */
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3 pos) {
        spawnItemEntity(level, stack, state, pos, null);
    }

    /**
     * Spawns an item entity in the world at an integer position without entity targeting.
     * <p>
     * This method converts the integer position to a vector position and delegates to
     * the main spawn method with a null UUID. See the main method for detailed implementation details.
     * @see #spawnItemEntity(Level, ItemStack, BlockState, Vec3, UUID)
     */
    public static void spawnItemEntity(Level level, ItemStack stack, @Nullable BlockState state, Vec3i pos) {
        spawnItemEntity(level, stack, state, Vec3.atLowerCornerOf(pos), null);
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
     * Adds an item stack to the player's inventory, dropping it on the ground if inventory is full
     * <p>
     * @param player The target player to receive the item
     * @param add The item stack to be added to the player's inventory
     */
    public static void addItemToPlayer(Player player, ItemStack add) {
        if (!player.getInventory().add(add)) {
            player.drop(add, false);
        }
    }

    /**
     * Adds an item stack to the player's inventory only if the player is not in creative mode
     * If the player is in creative mode, the item is not added
     * <p>
     * @param player The target player to receive the item
     * @param add The item stack to be added to the player's inventory
     */
    public static void addItemToPlayerNotCreative(Player player, ItemStack add) {
        if (!player.isCreative()) {
            addItemToPlayer(player, add);
        }
    }

    /**
     * Consumes the held item and gives a new item to the player in non-creative mode
     * If the player is in creative mode, no items are consumed
     * <p>
     * @param shrink The item stack currently held by the player (will be consumed)
     * @param player The target player to receive the new item
     * @param add The new item stack to be given to the player
     */
    public static void consumeItemAndGiveToPlayer(ItemStack shrink, Player player, ItemStack add) {
        if (!player.isCreative()) {
            shrinkItemStack(shrink, player, 1);
            addItemToPlayer(player, add);
        }
    }

    /**
     * Reduces the stack size of an item by the specified amount, unless the entity is a player in creative mode
     * <p>
     * @param itemStack The item stack to be shrunk
     * @param entity The entity that owns the item stack
     * @param decrement The amount to reduce the stack size by
     */
    public static void shrinkItemStack(ItemStack itemStack, LivingEntity entity, int decrement) {
        if (entity instanceof Player player && player.isCreative()) return;
        shrinkItemStack(itemStack, decrement);
    }

    /**
     * Reduces the stack size of an item by the specified amount
     * <p>
     * @param itemStack The item stack to be shrunk
     * @param decrement The amount to reduce the stack size by
     */
    public static void shrinkItemStack(ItemStack itemStack, int decrement) {
        if (itemStack.isEmpty()) return;
        itemStack.shrink(decrement);
    }

    /**
     * Applies damage to an item and breaks it if durability reaches zero, unless the entity is a player in creative mode
     * <p>
     * @param itemStack The item stack to damage and potentially break
     * @param entity The entity that owns and is using the item
     * @param decrement The amount of damage to apply to the item
     */
    public static void hurtAndBreakItemStack(ItemStack itemStack, LivingEntity entity, int decrement) {
        if ((entity instanceof Player player && player.isCreative()) || itemStack.isEmpty()) return;
        itemStack.hurtAndBreak(decrement, entity, entity.getEquipmentSlotForItem(itemStack));
    }

    /**
     * Reduces the quantity of items matching the target ItemStack in the ItemStackHandler.
     * <p>
     * This method searches through all slots in the ItemStackHandler for items that match
     * the target ItemStack, then reduces the total quantity
     * by the specified shrinkCount.
     * <p>
     * The reduction is applied across all matching stacks in the order they are found until
     * the total reduction reaches shrinkCount or all matching items are exhausted.
     * @param itemStackHandler The ItemStackHandler to remove items from
     * @param targetStack The target ItemStack to match against (can be null)
     * @param shrinkCount The total number of matching items to remove
     * @return The actual number of items that were removed
     */
    public static int shrinkMatchingItems(ItemStackHandler itemStackHandler, @Nullable ItemStack targetStack, int shrinkCount) {
        return shrinkMatchingItemsInRange(itemStackHandler, targetStack, shrinkCount, 0, itemStackHandler.getSlots());
    }

    /**
     * Reduces the quantity of items matching the target ItemStack in a specific slot range.
     * <p>
     * This method searches through the specified slot range in the ItemStackHandler for items
     * that match the target ItemStack, then reduces the total
     * quantity by the specified shrinkCount.
     * @param itemStackHandler The ItemStackHandler to remove items from
     * @param targetStack The target ItemStack to match against (can be null)
     * @param shrinkCount The total number of matching items to remove
     * @param startIndex The starting slot index (inclusive)
     * @param endIndex The ending slot index (exclusive)
     * @return The actual number of items that were removed
     */
    public static int shrinkMatchingItemsInRange(ItemStackHandler itemStackHandler, @Nullable ItemStack targetStack,
                                                 int shrinkCount, int startIndex, int endIndex) {
        int remaining = shrinkCount;
        for (int i = startIndex; i < endIndex && remaining > 0; i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            boolean matches = targetStack == null ? !stack.isEmpty() : (!stack.isEmpty() && ItemStack.isSameItem(stack, targetStack));
            if (matches) {
                int remove = Math.min(stack.getCount(), remaining);
                shrinkItemStack(stack, remove);
                remaining -= remove;
            }
        }
        return shrinkCount - remaining;
    }
}
