package mangopill.customized.common.block.handler;

import mangopill.customized.common.util.CreateItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class CIItemHandler<T extends CreateItemStackHandler> implements IItemHandler {
    protected final T entity;
    protected final IItemHandler itemHandler;

    protected CIItemHandler(T entity, IItemHandler itemHandler) {
        this.entity = entity;
        this.itemHandler = itemHandler;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    public T getEntity() {
        return entity;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
