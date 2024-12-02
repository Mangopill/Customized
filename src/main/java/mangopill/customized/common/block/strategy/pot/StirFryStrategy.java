package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotInteractionStrategy;
import mangopill.customized.common.tag.ModTag;
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

public class StirFryStrategy implements PotInteractionStrategy {
    //Will I use it in the future?
    @Override
    public void interact(@NotNull ItemStack itemStackInHand, @NotNull BlockState state,
                         @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull Player player, @NotNull InteractionHand hand,
                         @NotNull BlockHitResult result) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity){
            if (!canStirFry(itemStackInHand, state)){
                return;
            }
            stirFry(itemStackInHand, level, pos, player, hand, potBlockEntity);
        }
    }

    private boolean canStirFry(@NotNull ItemStack itemStackInHand, @NotNull BlockState state) {
        return !itemStackInHand.isEmpty() && !state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID) && itemStackInHand.is(ModTag.SPATULA);
    }

    private void stirFry(@NotNull ItemStack itemStackInHand, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, AbstractPotBlockEntity potBlockEntity) {
        potBlockEntity.stirFryAccelerate(itemStackInHand, player, hand);
        level.playSound(null, pos, SoundEvents.METAL_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
