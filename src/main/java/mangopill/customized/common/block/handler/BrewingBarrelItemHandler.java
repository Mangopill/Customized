package mangopill.customized.common.block.handler;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BrewingBarrelItemHandler implements IItemHandler {
    private final int inputSlot;
    private final IItemHandler itemHandler;
    private final Direction side;

    public BrewingBarrelItemHandler(IItemHandler itemHandler, @Nullable Direction side, int inputSlot) {
        this.inputSlot = inputSlot;
        this.itemHandler = itemHandler;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return slot < inputSlot ? itemHandler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            return slot < inputSlot ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot == inputSlot ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }
}
