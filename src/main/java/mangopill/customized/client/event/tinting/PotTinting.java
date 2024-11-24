package mangopill.customized.client.event.tinting;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.block.AbstractPotBlock.LID;

public class PotTinting {
    public static int getWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPotBlock potBlock) {
            if (!potBlock.canInputDrive() || !state.getValue(LID).equals(PotState.WITH_DRIVE)){
                return -1;
            }
            if (getter.getBlockEntity(pos) instanceof AbstractPotBlockEntity potBlockEntity){
                List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
                return getMaxValueColor(potBlockEntity.getLevel(), stackList);
            }
        }
        return -1;
    }
}
