package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.fluid.CFluidContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class CIFluidHandler<T extends Comparable<T>, V extends T> implements IFluidHandler {
    private final Level level;
    private final BlockPos pos;

    protected CIFluidHandler(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    abstract protected CFluidContent<T, V> getContent();

    abstract protected CFluidContent<T, V> getContentForFill();

    abstract protected boolean canInput();

    abstract protected boolean canOutput();

    protected void updateDriveState(FluidAction action, CFluidContent<T, V> contents) {
        if (!action.execute()) return;
        if (canInput()) {
            BlockState blockState = level.getBlockState(pos);
            level.setBlockAndUpdate(pos, blockState.setValue(contents.getProperty(), contents.getValue()));
        }
    }

    protected void updateWithoutDriveState(FluidAction action, CFluidContent<T, V> contents) {
        if (!action.execute()) return;
        if (canOutput()) {
            BlockState blockState = level.getBlockState(pos);
            level.setBlockAndUpdate(pos, blockState.setValue(contents.getProperty(), contents.getValue()));
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        CFluidContent<T, V> contents = this.getContent();
        return new FluidStack(contents.getFluid(), contents.getTotalAmount());
    }

    @Override
    public int getTankCapacity(int i) {
        CFluidContent<T, V> contents = this.getContent();
        return contents.getTotalAmount();
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        if (fluidStack.isEmpty()) return 0;
        CFluidContent<T, V> contents = getContentForFill();
        if (contents.getFluid() != Fluids.EMPTY && !fluidStack.is(contents.getFluid())) return 0;
        int amount = fluidStack.getAmount();
        if (canInput()) {
            if (amount >= contents.getTotalAmount()){
                updateDriveState(fluidAction, contents);
                return contents.getTotalAmount();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        if (fluidStack.isEmpty()) {
            return FluidStack.EMPTY;
        } else {
            return fluidStack.is(this.getContent().getFluid())
                    && fluidStack.getComponents().isEmpty()
                    && canOutput()
                    && fluidStack.getAmount() >= getContent().getTotalAmount()
                    ? this.drain(fluidAction) : FluidStack.EMPTY;
        }
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction fluidAction) {
        return maxDrain <= 0 ? FluidStack.EMPTY : this.drain(fluidAction);
    }

    protected FluidStack drain(FluidAction fluidAction) {
        CFluidContent<T, V> content = this.getContent();
        updateWithoutDriveState(fluidAction, content);
        return new FluidStack(content.getFluid(), content.getTotalAmount());
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }
}
