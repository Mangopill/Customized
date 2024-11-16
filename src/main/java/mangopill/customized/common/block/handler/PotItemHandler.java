package mangopill.customized.common.block.handler;

import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PotItemHandler implements IItemHandler {
    private static int ingredientInput;
    private static int seasoningInput;
    private static final int SPICE_INPUT = 1;
    private static final int OUTPUT = 1;
    private final IItemHandler itemHandler;
    private final Direction side;

    public PotItemHandler(IItemHandler itemHandler, @Nullable Direction side, int ingredientCount, int seasoningCount) {
        this.itemHandler = itemHandler;
        ingredientInput = ingredientCount;
        seasoningInput = seasoningCount;
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
        if (side == null || side.equals(Direction.UP)) {
            return slot < ingredientInput ? itemHandler.insertItem(slot, stack, simulate) : stack;
        } else {
            if (slot < seasoningInput + ingredientInput && slot >= ingredientInput){
                return stack.is(ModTag.SEASONING) ? itemHandler.insertItem(slot, stack, simulate) : stack;
            }
            if (slot == seasoningInput + ingredientInput){
                return stack.is(ModTag.FAMOUS_SPICE) ? itemHandler.insertItem(slot, stack, simulate) : stack;
            }
            return stack;
        }
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            return slot < ingredientInput ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot == seasoningInput + ingredientInput + SPICE_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
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
