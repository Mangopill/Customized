package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.entity.CrateBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.component.CItemMatchMode.*;

public class CrateItemHandler extends CIItemHandler<CrateBlockEntity> {

    public CrateItemHandler(CrateBlockEntity entity, IItemHandler itemHandler) {
        super(entity, itemHandler);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (getEntity().hasInput() && !simpleTest(getEntity().getItemStackListInBlockEntity(true).getFirst(), stack, SAME_ITEM_SAME_COMPONENTS)) {
            return stack;
        }
        return slot < getEntity().getAllSlot() ? getItemHandler().insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot <= getEntity().getAllSlot() ? getItemHandler().extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
