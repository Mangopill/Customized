package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.strategy.base.IPotInteractionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public record CancellableStrategy(TagKey<Item> tagKey) implements IPotInteractionStrategy {
    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        return itemStackInHand.is(tagKey);
    }
}
