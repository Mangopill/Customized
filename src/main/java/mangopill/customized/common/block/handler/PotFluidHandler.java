package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.fluid.PotFluidContent;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PotFluidHandler extends ModIFluidHandler<PotState, PotState> {

    public PotFluidHandler(Level level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    protected PotFluidContent getContent() {
        return PotFluidContent.getContent(getLevel(), getPos());
    }
    @Override
    protected PotFluidContent getContentForFill() {
        return PotFluidContent.getContentForFill(getLevel(), getPos());
    }

    @Override
    protected boolean canInput(){
        BlockState blockState = getLevel().getBlockState(getPos());
        return blockState.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID);
    }

    @Override
    protected boolean canOutput(){
        BlockState blockState = getLevel().getBlockState(getPos());
        return blockState.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE);
    }
}
