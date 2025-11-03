package mangopill.customized.common.item;

import mangopill.customized.common.util.CItemStackHandlerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.component.PlateComponentUtil.getItemStackHandler;

public class CrateItem extends BlockItem {
    public CrateItem(Supplier<Block> block, Properties properties) {
        super(block.get(), properties);
    }

    public boolean hasInput(ItemStack stack) {
        return CItemStackHandlerHelper.hasInput(getItemStackHandler(stack), getItemStackHandler(stack).getSlots());
    }

    public List<ItemStack> getItemStackListInCrate(ItemStack stack) {
        return CItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, getItemStackHandler(stack).getSlots());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return hasInput(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.ceil((double) getNonEmptySlotCount(getItemStackHandler(stack), 18) / 18 * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 16755200;
    }
}
