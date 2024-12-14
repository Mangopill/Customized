package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.fluid.ModFluidContent;
import mangopill.customized.common.block.fluid.PotFluidContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public abstract class ModIFluidHandler implements IFluidHandler {
    private final Level level;
    private final BlockPos pos;

    public ModIFluidHandler(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    abstract protected ModFluidContent<?> getContent();

    abstract protected ModFluidContent<?> getContentForFill();

    abstract protected boolean canInput();

    abstract protected boolean canOutput();

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        ModFluidContent<?> contents = this.getContent();
        return new FluidStack(contents.getFluid(), contents.getTotalAmount());
    }

    @Override
    public int getTankCapacity(int i) {
        ModFluidContent<?> contents = this.getContent();
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
        ModFluidContent<?> contents = getContentForFill();
        if (contents.getFluid() != Fluids.EMPTY && !fluidStack.is(contents.getFluid())) {
            return 0;
        }
        int amount = fluidStack.getAmount();
        if (canInput()) {
            if (amount >= contents.getTotalAmount()){
                return contents.getTotalAmount();
            } else {
                return 0;
            }
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
                    && fluidStack.getAmount() >= getContent().getTotalAmount()
                    ? this.drain(fluidAction) : FluidStack.EMPTY;
        }
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction fluidAction) {
        return maxDrain <= 0 ? FluidStack.EMPTY : this.drain(fluidAction);
    }

    abstract protected FluidStack drain(FluidAction fluidAction);

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }
}
