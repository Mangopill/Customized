package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.SaltPanBlock;
import mangopill.customized.common.block.fluid.SaltPanContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SaltPanFluidHandler extends ModIFluidHandler<Boolean, Boolean> {

    public SaltPanFluidHandler(Level level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    protected SaltPanContent getContent() {
        return SaltPanContent.getContent(getLevel(), getPos());
    }

    @Override
    protected SaltPanContent getContentForFill() {
        return SaltPanContent.getContentForFill(getLevel(), getPos());
    }

    @Override
    protected boolean canInput(){
        BlockState blockState = getLevel().getBlockState(getPos());
        return blockState.getValue(SaltPanBlock.WITH_WATER).equals(false);
    }

    @Override
    protected boolean canOutput(){
        BlockState blockState = getLevel().getBlockState(getPos());
        return blockState.getValue(SaltPanBlock.WITH_WATER).equals(true);
    }
}
