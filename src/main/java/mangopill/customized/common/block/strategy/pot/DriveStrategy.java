package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotInteractionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DriveStrategy implements PotInteractionStrategy {
    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                         Level level, BlockPos pos,
                         Player player, InteractionHand hand,
                         BlockHitResult result) {
        if (canAdd(itemStackInHand, state)){
            addDrive(itemStackInHand, state, level, pos, player);
            return true;
        }
        if (canClear(itemStackInHand, state)){
            clearDrive(itemStackInHand, state, level, pos, player);
            return true;
        }
        return false;
    }

    private boolean canAdd(ItemStack itemStackInHand, BlockState state) {
        return !itemStackInHand.isEmpty() && state.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID) && itemStackInHand.is(Items.WATER_BUCKET);
    }

    private boolean canClear(ItemStack itemStackInHand, BlockState state) {
        return !itemStackInHand.isEmpty() && state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE) && itemStackInHand.is(Items.BUCKET);
    }

    private void addDrive(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITH_DRIVE));
        if (!player.isCreative()) {
            itemStackInHand.shrink(1);
            if (!player.getInventory().add(Items.BUCKET.getDefaultInstance())) {
                player.drop(Items.BUCKET.getDefaultInstance(), false);
            }
        }
        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private void clearDrive(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITHOUT_LID));
        if (!player.isCreative()) {
            itemStackInHand.shrink(1);
            if (!player.getInventory().add(Items.WATER_BUCKET.getDefaultInstance())) {
                player.drop(Items.WATER_BUCKET.getDefaultInstance(), false);
            }
        }
        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
