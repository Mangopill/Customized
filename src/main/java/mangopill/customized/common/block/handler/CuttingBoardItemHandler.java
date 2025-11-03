package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.entity.CuttingBoardBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class CuttingBoardItemHandler extends CIItemHandler<CuttingBoardBlockEntity> {
    public CuttingBoardItemHandler(CuttingBoardBlockEntity entity, IItemHandler itemHandler) {
        super(entity, itemHandler);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return slot < getEntity().getAllSlot() ? getItemHandler().insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot <= getEntity().getAllSlot() && getEntity().getTimes() <= 0 ? getItemHandler().extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
