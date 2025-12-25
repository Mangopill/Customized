package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotInteractionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public record LidStrategy(ItemStack lid, boolean canInputDrive) implements PotInteractionStrategy {

    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                            Level level, BlockPos pos,
                            Player player, InteractionHand hand,
                            BlockHitResult result) {
        if (itemStackInHand.isEmpty() && state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            removeTheLid(state, level, pos, player);
            return true;
        }
        if (ItemStack.isSameItem(itemStackInHand, lid) && !state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            if (!canInputDrive || state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE)) {
                addLid(itemStackInHand, state, level, pos, player);
                return true;
            }
        }
        return false;
    }

    private void addLid(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player) {
        shrinkItemStack(itemStackInHand, player, 1);
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITH_LID));
        level.playSound(null, pos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private void removeTheLid(BlockState state, Level level, BlockPos pos, Player player) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, canInputDrive ? PotState.WITH_DRIVE : PotState.WITHOUT_LID));
        addItemToPlayerNotCreative(player, lid.copy());
        level.playSound(null, pos, SoundEvents.DECORATED_POT_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
