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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.common.block.state.PotState.WITHOUT_LID;

public class LidStrategy implements PotInteractionStrategy {
    private final ItemStack lid;
    private final boolean canInputDrive;

    public LidStrategy(ItemStack lid, boolean canInputDrive) {
        this.lid = lid;
        this.canInputDrive = canInputDrive;
    }

    @Override
    public void interact(@NotNull ItemStack itemStackInHand, @NotNull BlockState state,
                                          @NotNull Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult result) {
        if (itemStackInHand.isEmpty() && state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)){
            if (canInputDrive){
                removeTheLidDrive(state, level, pos, player);
            } else {
                removeTheLid(state, level, pos, player);
            }
        }
       if (itemStackInHand.is(lid.getItem()) && !state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)){
           if (canInputDrive){
               if (state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE)){
                   addLid(itemStackInHand, state, level, pos);
               }
           } else {
               addLid(itemStackInHand, state, level, pos);
           }
       }
    }

    private void addLid(@NotNull ItemStack itemStackInHand, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        itemStackInHand.shrink(1);
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITH_LID));
        level.playSound(null, pos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private void removeTheLid(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, WITHOUT_LID));
        player.getInventory().add(lid.copy());
        level.playSound(null, pos, SoundEvents.DECORATED_POT_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private void removeTheLidDrive(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        level.setBlockAndUpdate(pos, state.setValue(AbstractPotBlock.LID, PotState.WITH_DRIVE));
        player.getInventory().add(lid.copy());
        level.playSound(null, pos, SoundEvents.DECORATED_POT_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    public ItemStack getLid() {
        return lid;
    }

    public boolean isCanInputDrive() {
        return canInputDrive;
    }
}
