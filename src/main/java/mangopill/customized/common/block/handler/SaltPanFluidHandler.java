package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.SaltPanBlock;
import mangopill.customized.common.block.fluid.SaltPanContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SaltPanFluidHandler extends CIFluidHandler<Boolean, Boolean> {

    public SaltPanFluidHandler(Level level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    protected SaltPanContent getContent() {
        return SaltPanContent.getContent(level, pos);
    }

    @Override
    protected SaltPanContent getContentForFill() {
        return SaltPanContent.getContentForFill(level, pos);
    }

    @Override
    protected boolean canInput(){
        BlockState blockState = level.getBlockState(pos);
        return blockState.getValue(SaltPanBlock.WITH_WATER).equals(false);
    }

    @Override
    protected boolean canOutput(){
        BlockState blockState = level.getBlockState(pos);
        return blockState.getValue(SaltPanBlock.WITH_WATER).equals(true);
    }
}
