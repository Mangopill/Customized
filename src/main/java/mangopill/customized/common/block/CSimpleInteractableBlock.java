package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.CBasicCookingBlockEntity;
import mangopill.customized.common.recipe.CRecipeInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public interface CSimpleInteractableBlock {
    default ItemInteractionResult simpleInteract(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos,
                                                 Player player, InteractionHand hand, SoundEvent output, SoundEvent insert) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CBasicCookingBlockEntity<? extends CRecipeInterface<RecipeWrapper>> entity) {
            entity.interact(itemStackInHand, player, level, hand, state, pos, output, insert);
        }
        return ItemInteractionResult.SUCCESS;
    }
}