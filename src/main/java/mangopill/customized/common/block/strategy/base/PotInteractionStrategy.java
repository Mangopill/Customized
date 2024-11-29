package mangopill.customized.common.block.strategy.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface  PotInteractionStrategy {
    void interact(@NotNull ItemStack itemStackInHand, @NotNull BlockState state,
                                   @NotNull Level level, @NotNull BlockPos pos,
                                   @NotNull Player player, @NotNull InteractionHand hand,
                                   @NotNull BlockHitResult result
    );
}
