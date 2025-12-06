package mangopill.customized.common.block.strategy.pot;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.items.IItemHandler;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public record FluidContainerInteractionStrategy() implements PotInteractionStrategy {

    @Override
    public boolean interact(ItemStack itemStackInHand, BlockState state,
                            Level level, BlockPos pos,
                            Player player, InteractionHand hand,
                            BlockHitResult result) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractPotBlockEntity potBlockEntity)) {
            return false;
        }
        IItemHandler playerInventory = player.getCapability(Capabilities.ItemHandler.ENTITY);
        if (playerInventory == null) {
            return false;
        }
        FluidActionResult fillResult = FluidUtil.tryFillContainerAndStow(itemStackInHand, potBlockEntity.getFluidHandler(), playerInventory, Integer.MAX_VALUE, player, true);
        if (fillResult.isSuccess()) {
            consumeItemAndGiveToPlayer(itemStackInHand, player, fillResult.getResult().copy());
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
            return true;
        }
        FluidActionResult emptyResult = FluidUtil.tryEmptyContainerAndStow(itemStackInHand, potBlockEntity.getFluidHandler(), playerInventory, Integer.MAX_VALUE, player, true);
        if (emptyResult.isSuccess()) {
            consumeItemAndGiveToPlayer(itemStackInHand, player, emptyResult.getResult().copy());
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
            return true;
        }
        return false;
    }
}