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

public class StirFryStrategy implements PotInteractionStrategy {
    //Will I use it in the future?
    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                         Level level, BlockPos pos,
                         Player player, InteractionHand hand,
                         BlockHitResult result) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity){
            if (!canStirFry(itemStackInHand, state)){
                return false;
            }
            stirFry(itemStackInHand, level, pos, player, hand, potBlockEntity);
            return true;
        }
        return false;
    }

    private boolean canStirFry(ItemStack itemStackInHand, BlockState state) {
        return !itemStackInHand.isEmpty() && !state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID) && itemStackInHand.is(ModTag.SPATULA);
    }

    private void stirFry(ItemStack itemStackInHand, Level level, BlockPos pos, Player player, InteractionHand hand, AbstractPotBlockEntity potBlockEntity) {
        potBlockEntity.stirFryAccelerate(itemStackInHand, player, hand);
        level.playSound(null, pos, SoundEvents.METAL_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
    }
}
