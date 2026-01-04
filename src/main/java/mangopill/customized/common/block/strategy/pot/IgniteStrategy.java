package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.IPotInteractionStrategy;
import mangopill.customized.common.tag.CommonTag;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static mangopill.customized.common.util.InteractUtil.*;
import static mangopill.customized.common.util.SensoryUtil.*;

public record IgniteStrategy() implements IPotInteractionStrategy {
    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                         Level level, BlockPos pos,
                         Player player, InteractionHand hand,
                         BlockHitResult result) {
        if (canIgnite(itemStackInHand, state)){
            ignite(itemStackInHand, state, level, pos, player, hand);
            return true;
        }
        return false;
    }

    private boolean canIgnite(ItemStack itemStackInHand, BlockState state) {
        return !itemStackInHand.isEmpty() && state.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID) && itemStackInHand.is(CommonTag.CREEPER_IGNITERS);
    }

    private void ignite(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITH_DRIVE));
        hurtAndBreakItemStack(itemStackInHand, player, 1);
        playSound(level, null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
