package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.SaltPanBlock;
import mangopill.customized.common.block.fluid.SaltPanContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class SaltPanFluidHandler extends ModIFluidHandler {

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

    protected void updateDriveState(FluidAction action, SaltPanContent contents) {
        if (!action.execute()) {
            return;
        }
        BlockState blockState = getLevel().getBlockState(getPos());
        if (canInput()) {
            getLevel().setBlockAndUpdate(getPos(), blockState.setValue(SaltPanBlock.WITH_WATER, contents.getProperty()));
        }
    }

    protected void updateWithoutDriveState(FluidAction action, SaltPanContent contents) {
        if (!action.execute()) {
            return;
        }
        BlockState blockState = getLevel().getBlockState(getPos());
        if (canOutput()) {
            getLevel().setBlockAndUpdate(getPos(), blockState.setValue(SaltPanBlock.WITH_WATER, contents.getProperty()));
        }
    }

    @Override
    public int fill(@NotNull FluidStack fluidStack, @NotNull FluidAction fluidAction) {
        SaltPanContent contents = getContentForFill();
        if (super.fill(fluidStack, fluidAction) == contents.getTotalAmount()) {
            updateDriveState(fluidAction, contents);
        }
        return super.fill(fluidStack, fluidAction);
    }

    @Override
    protected FluidStack drain(FluidAction fluidAction) {
        SaltPanContent content = this.getContent();
        updateWithoutDriveState(fluidAction, content);
        return new FluidStack(content.getFluid(), content.getTotalAmount());
    }
}
