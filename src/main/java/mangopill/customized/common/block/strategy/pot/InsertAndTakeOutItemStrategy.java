package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotInteractionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class InsertAndTakeOutItemStrategy implements PotInteractionStrategy {
    // Ensure this strategy is at the end of the registration
    @Override
    public void interact(@NotNull ItemStack itemStackInHand, @NotNull BlockState state,
                         @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull Player player, @NotNull InteractionHand hand,
                         @NotNull BlockHitResult result) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity){
            if (itemStackInHand.isEmpty()){
                if (!state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID) && player.isShiftKeyDown()){
                    takeOut(state, level, pos, potBlockEntity);
                }
            } else {
                insert(itemStackInHand, level, pos, potBlockEntity);
            }
        }
    }

    private void insert(@NotNull ItemStack itemStackInHand, @NotNull Level level, @NotNull BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        potBlockEntity.insertItem(itemStackInHand);
        level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private void takeOut(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        potBlockEntity.takeOutItem(level, state, pos);
        level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
