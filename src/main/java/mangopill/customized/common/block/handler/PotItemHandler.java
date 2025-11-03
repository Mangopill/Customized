package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.containsSameItem;

public class PotItemHandler extends CIItemHandler<AbstractPotBlockEntity> {
    private final int ingredientInput;
    private final int seasoningInput;
    private final int spiceInput;

    public PotItemHandler(AbstractPotBlockEntity entity, IItemHandler itemHandler) {
        super(entity, itemHandler);
        this.ingredientInput = entity.getIngredientInput();
        this.seasoningInput = entity.getSeasoningInput();
        this.spiceInput = entity.getSpiceInput();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.is(ModTag.SEASONING)) {
            return slot < seasoningInput + ingredientInput && slot >= ingredientInput ? getItemHandler().insertItem(slot, stack, simulate) : stack;
        }
        if (stack.is(ModTag.FAMOUS_SPICE)) {
            return slot == seasoningInput + ingredientInput ? getItemHandler().insertItem(slot, stack, simulate) : stack;
        }
        if (stack.getItem() instanceof AbstractPlateItem || containsSameItem(List.of(getEntity().getContainerItem().getItems()), stack)) {
            return slot == seasoningInput + ingredientInput + spiceInput ? getItemHandler().insertItem(slot, stack, simulate) : stack;
        }
        return slot < ingredientInput ? getItemHandler().insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot == seasoningInput + ingredientInput + spiceInput ? getItemHandler().extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
