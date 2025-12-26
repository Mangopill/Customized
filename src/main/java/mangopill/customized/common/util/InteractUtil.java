package mangopill.customized.common.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InteractUtil {
    private InteractUtil() {}

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
}
