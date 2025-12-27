package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.strategy.base.IPotInteractionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;

public record FluidContainerStrategy() implements IPotInteractionStrategy {

    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                            Level level, BlockPos pos,
                            Player player, InteractionHand hand,
                            BlockHitResult result) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractPotBlockEntity potBlockEntity)) {
            return false;
        }
        return FluidUtil.interactWithFluidHandler(player, hand, potBlockEntity.getFluidHandler());
    }
}