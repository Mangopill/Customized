package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.entity.BrewingBarrelBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class BrewingBarrelItemHandler extends CIItemHandler<BrewingBarrelBlockEntity> {

    public BrewingBarrelItemHandler(BrewingBarrelBlockEntity entity, IItemHandler itemHandler) {
        super(entity, itemHandler);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (entity.getContainerItem().test(stack)) {
            return slot == entity.getInputSlot() ? itemHandler.insertItem(slot, stack, simulate) : stack;
        }
        return slot < entity.getInputSlot() ? itemHandler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot == entity.getInputSlot() ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
