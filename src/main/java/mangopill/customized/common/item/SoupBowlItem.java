package mangopill.customized.common.item;

import mangopill.customized.common.block.entity.CasseroleBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.PlateComponentUtil.*;

public class SoupBowlItem extends AbstractPlateItem {
    public SoupBowlItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        ItemStack itemInHand = context.getItemInHand();
        Player player = context.getPlayer();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof CasseroleBlockEntity potBlockEntity && player.isShiftKeyDown()) {
            List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
            ItemStackHandler newItemStackHandler = copyItemStackHandlerByComponent(itemInHand);
            stackList.forEach(stack -> insertItem(stack, newItemStackHandler));
            List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
            potBlockEntity.itemStackHandlerChanged();
            updateAll(itemInHand, newItemStackHandler, getFoodPropertyByPropertyValue(level, newStackList, true), getConsumptionCount(newStackList), getConsumptionCount(newStackList));
            return InteractionResult.SUCCESS;
        }
        return player.isShiftKeyDown() ? super.useOn(context) : InteractionResult.PASS;
    }


}
