package mangopill.customized.client.event.tinting;

import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.SoupBowlItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.getMaxValueColor;
import static mangopill.customized.common.block.AbstractPlateBlock.DRIVE;
import static mangopill.customized.common.block.AbstractPotBlock.LID;

public class Tinting {
    public static int getPotWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPotBlock potBlock) {
            if (!state.getValue(LID).equals(PotState.WITH_DRIVE)){
                return -1;
            }
            if (getter.getBlockEntity(pos) instanceof AbstractPotBlockEntity potBlockEntity){
                List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
                return getMaxValueColor(potBlockEntity.getLevel(), stackList);
            }
        }
        return -1;
    }
    public static int getPlateBlockWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPlateBlock) {
            if (!state.getValue(DRIVE).equals(PlateState.WITH_DRIVE)){
                return -1;
            }
            if (getter.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateBlockEntity){
                List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(true);
                return getMaxValueColor(plateBlockEntity.getLevel(), stackList);
            }
        }
        return -1;
    }
    public static int getPlateItemWaterColor(ItemStack stack) {
        if (stack.getItem() instanceof SoupBowlItem soupBowlItem && soupBowlItem.hasInput(stack)) {
            List<ItemStack> stackList = soupBowlItem.getItemStackListInPlate(stack, true);
            if (Minecraft.getInstance().player != null) {
                return getMaxValueColor(Minecraft.getInstance().level, stackList);
            }
        }
        return -1;
    }
}
