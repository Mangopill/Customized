package mangopill.customized.common.util.component;

import mangopill.customized.common.util.CItemStackHandlerHelper;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Enumeration defining various matching strategies for comparing {@link ItemStack} instances.
 * Each enumeration constant represents a specific comparison logic encapsulated as a {@link BiPredicate}.
 * <p>
 * This enum provides a set of predefined comparison modes that can be used to determine
 * whether two item stacks are considered "matching" according to different criteria.
 * <p>
 * Usage example:
 * {@link CItemStackHandlerHelper#simpleTest(ItemStack, ItemStack, IItemMatchMode)}
 * <pre>{@code
 * // Check if two stacks are the same item type
 * boolean sameItem = ItemMatchMode.SAME_ITEM.getComparator().test(stack1, stack2);
 * // Also
 * boolean sameItem = simpleTest(stack1, stack2, CItemMatchMode.SAME_ITEM)
 * }</pre>
 */
public enum CItemMatchMode implements IItemMatchMode<ItemStack, ItemStack> {
    SAME_ITEM(ItemStack::isSameItem),
    SAME_COUNT((stack, target) -> stack.getCount() == target.getCount()),
    ANY_SAME_COMPONENT((stack, target) -> DataComponentPredicate.allOf(stack.getComponents()).test(target)),
    ALL_SAME_COMPONENTS((stack, target) -> (stack.isEmpty() && target.isEmpty()) || Objects.equals(stack.getComponents(), target.getComponents())),
    /**
     * Vanilla-compatible matching mode that compares both item type and data components.
     * Returns {@code true} when the item types are the same and the data component maps are equal,
     * or when both stacks are empty.
     * <p>
     * This mode provides compatibility with vanilla Minecraft's matching logic and uses
     * {@link ItemStack#isSameItemSameComponents(ItemStack, ItemStack)} internally.
     */
    SAME_ITEM_SAME_COMPONENTS(ItemStack::isSameItemSameComponents),
    SAME_ITEM_ANY_COMPONENTS(SAME_ITEM.comparator.and(ANY_SAME_COMPONENT.comparator));


    private final BiPredicate<ItemStack, ItemStack> comparator;

    CItemMatchMode(BiPredicate<ItemStack, ItemStack> comparator) {
        this.comparator = comparator;
    }

    @Override
    public BiPredicate<ItemStack, ItemStack> getComparator() {
        return comparator;
    }
}
