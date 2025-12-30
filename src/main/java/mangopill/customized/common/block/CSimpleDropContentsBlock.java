package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.CBasicCookingBlockEntity;
import net.minecraft.core.*;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface CSimpleDropContentsBlock {
    default void dropContents(BlockState state, Level level, BlockPos pos, BlockState newState, Block block) {
        if (state.getBlock() == newState.getBlock()) return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        NonNullList<ItemStack> stackNonNullList = getCustomStackList(state, level, pos, newState, block).isEmpty()
                ? ((blockEntity instanceof CBasicCookingBlockEntity<?> cookingBlockEntity)
                    ? NonNullList.copyOf(cookingBlockEntity.getItemStackListInBlockEntity(true))
                    : getCustomStackList(state, level, pos, newState, block))
                : getCustomStackList(state, level, pos, newState, block);
        Containers.dropContents(level, pos, stackNonNullList);
        level.updateNeighbourForOutputSignal(pos, block);
    }

    default NonNullList<ItemStack> getCustomStackList(BlockState state, Level level, BlockPos pos, BlockState newState, Block block) {
        return NonNullList.create();
    }
}
