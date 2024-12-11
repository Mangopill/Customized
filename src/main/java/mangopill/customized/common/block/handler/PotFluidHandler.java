package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.fluid.PotFluidContent;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class PotFluidHandler implements IFluidHandler {
    private final Level level;
    private final BlockPos pos;

    public PotFluidHandler(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    protected PotFluidContent getContent() {
        return PotFluidContent.getContent(level, pos);
    }

    protected PotFluidContent getContentForFill() {
        return PotFluidContent.getContentForFill(level, pos);
    }

    protected boolean canInput(){
        BlockState blockState = level.getBlockState(pos);
        return blockState.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID);
    }

    protected boolean canOutput(){
        BlockState blockState = level.getBlockState(pos);
        return blockState.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE);
    }

    protected void updateDriveState(FluidAction action) {
        if (!action.execute()) {
            return;
        }
        BlockState blockState = level.getBlockState(pos);
        if (canInput()) {
            level.setBlockAndUpdate(pos, blockState.setValue(AbstractPotBlock.LID, PotState.WITH_DRIVE));
        }
    }

    protected void updateWithoutDriveState(FluidAction action) {
        if (!action.execute()) {
            return;
        }
        BlockState blockState = level.getBlockState(pos);
        if (canOutput()) {
            level.setBlockAndUpdate(pos, blockState.setValue(AbstractPotBlock.LID, PotState.WITHOUT_LID));
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        PotFluidContent contents = this.getContent();
        return new FluidStack(contents.getFluid(), contents.getTotalAmount());
    }

    @Override
    public int getTankCapacity(int i) {
        PotFluidContent contents = this.getContent();
        return contents.getTotalAmount();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(@NotNull FluidStack fluidStack, @NotNull FluidAction fluidAction) {
        if (fluidStack.isEmpty()) {
            return 0;
        }
        PotFluidContent potFluidContent = getContent();
        if (potFluidContent.getFluid() != Fluids.EMPTY && !fluidStack.is(potFluidContent.getFluid())) {
            return 0;
        }
        int amount = fluidStack.getAmount();
        if (amount == 100) {
            amount = amount + 900;
        }
        System.out.println("fluidStack.getAmount()" + amount);
        if (canInput() && amount >= FluidType.BUCKET_VOLUME) {
            PotFluidContent contents = this.getContentForFill();
            updateDriveState(fluidAction);
            return contents.getTotalAmount();
        } else {
            return 0;
        }
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack fluidStack, @NotNull FluidAction fluidAction) {
        if (fluidStack.isEmpty()) {
            return FluidStack.EMPTY;
        } else {
            return fluidStack.is(this.getContent().getFluid())
                    && fluidStack.getComponents().isEmpty()
                    && canOutput()
                    && fluidStack.getAmount() >= FluidType.BUCKET_VOLUME
                    ? this.drain(fluidAction) : FluidStack.EMPTY;
        }
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction fluidAction) {
        return maxDrain <= 0 ? FluidStack.EMPTY : this.drain(fluidAction);
    }

    protected FluidStack drain(FluidAction fluidAction) {
        PotFluidContent content = this.getContent();
        updateWithoutDriveState(fluidAction);
        return new FluidStack(content.getFluid(), content.getTotalAmount());
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }
}
