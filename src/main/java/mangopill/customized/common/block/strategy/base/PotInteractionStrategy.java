package mangopill.customized.common.block.strategy.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface  PotInteractionStrategy {
    boolean interact(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                     InteractionHand hand, BlockHitResult result
    );
}
