package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.fluid.PotFluidContent;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.*;
import org.jetbrains.annotations.NotNull;

public class PotFluidHandler extends ModIFluidHandler {

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

    protected void updateDriveState(FluidAction action, PotFluidContent contents) {
        if (!action.execute()) {
            return;
        }
        if (canInput()) {
            BlockState blockState = getLevel().getBlockState(getPos());
            getLevel().setBlockAndUpdate(getPos(), blockState.setValue(AbstractPotBlock.LID, contents.getProperty()));
        }
    }

    protected void updateWithoutDriveState(FluidAction action, PotFluidContent contents) {
        if (!action.execute()) {
            return;
        }
        if (canOutput()) {
            BlockState blockState = getLevel().getBlockState(getPos());
            getLevel().setBlockAndUpdate(getPos(), blockState.setValue(AbstractPotBlock.LID, contents.getProperty()));
        }
    }

    @Override
    public int fill(@NotNull FluidStack fluidStack, @NotNull FluidAction fluidAction) {
        PotFluidContent contents = getContentForFill();
        if (super.fill(fluidStack, fluidAction) == contents.getTotalAmount()) {
            updateDriveState(fluidAction, contents);
        }
        return super.fill(fluidStack, fluidAction);
    }

    @Override
    protected FluidStack drain(FluidAction fluidAction) {
        PotFluidContent content = this.getContent();
        updateWithoutDriveState(fluidAction, content);
        return new FluidStack(content.getFluid(), content.getTotalAmount());
    }
}
