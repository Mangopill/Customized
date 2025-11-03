package mangopill.customized.common.util.component;

import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.record.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class CrateComponentUtil {
    private CrateComponentUtil() {
    }

    public static ItemStackHandler getItemStackHandler(ItemStack stack) {
        return stack.getOrDefault(CDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL).itemStackHandler();
    }

    public static void setItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.set(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }
}
