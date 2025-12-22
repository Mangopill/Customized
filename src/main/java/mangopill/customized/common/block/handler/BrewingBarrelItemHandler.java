package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.entity.BrewingBarrelBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public class BrewingBarrelItemHandler extends CIItemHandler<BrewingBarrelBlockEntity> {

    public BrewingBarrelItemHandler(BrewingBarrelBlockEntity entity, IItemHandler itemHandler) {
        super(entity, itemHandler);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (containsSameItem(List.of(getEntity().getContainerItem().getItems()), stack)) {
            return slot == getEntity().getInputSlot() ? getItemHandler().insertItem(slot, stack, simulate) : stack;
        }
        return slot < getEntity().getInputSlot() ? getItemHandler().insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot == getEntity().getInputSlot() ? getItemHandler().extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
