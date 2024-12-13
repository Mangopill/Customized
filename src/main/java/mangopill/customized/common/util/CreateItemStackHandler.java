package mangopill.customized.common.util;

import net.neoforged.neoforge.items.ItemStackHandler;

public interface CreateItemStackHandler {
    default ItemStackHandler createItemStackHandler(int allSlot) {
        return new ItemStackHandler(allSlot)
        {
            @Override
            protected void onContentsChanged(int slot) {
                itemStackHandlerChanged();
            }

            @Override
            protected void onLoad() {
                itemStackHandlerChanged();
            }
        };
    }

    void itemStackHandlerChanged();
}
