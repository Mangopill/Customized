package mangopill.customized.common.block.handler;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.tag.CTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class PotFluidHandler implements IFluidHandler {
    private final AbstractPotBlockEntity entity;
    private FluidStack storedFluid;
    private final int capacity;

    public PotFluidHandler(AbstractPotBlockEntity entity, int capacity) {
        this.entity = entity;
        this.capacity = capacity;
        this.storedFluid = FluidStack.EMPTY;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return storedFluid.copy();
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction action) {
        if (fluidStack.isEmpty() || entity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            return 0;
        }
        if (!storedFluid.isEmpty() && !FluidStack.isSameFluid(storedFluid, fluidStack)) {
            return 0;
        }
        int fillAmount = Math.min(fluidStack.getAmount(), getSpace());
        if (fillAmount > 0 && action.execute()) {
            storedFluid = new FluidStack(fluidStack.getFluid(), Math.min(storedFluid.getAmount() + fillAmount, capacity));
        }
        if (isFull() && entity.getLevel() != null) {
            entity.getLevel().setBlockAndUpdate(entity.getBlockPos(), entity.getBlockState().setValue(AbstractPotBlock.LID, PotState.WITH_DRIVE));
        }
        entity.itemStackHandlerChanged();
        return fillAmount;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction action) {
        if (fluidStack.isEmpty() || !FluidStack.isSameFluid(storedFluid, fluidStack) || entity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            return FluidStack.EMPTY;
        }
        return drain(fluidStack.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0 || storedFluid.isEmpty()) {
            return FluidStack.EMPTY;
        }
        int drained = Math.min(storedFluid.getAmount(), maxDrain);
        FluidStack result = new FluidStack(storedFluid.getFluidHolder(), drained);
        if (action.execute()) {
            storedFluid.shrink(Math.min(drained, storedFluid.getAmount()));
        }
        if (!isFull() && entity.getLevel() != null) {
            entity.getLevel().setBlockAndUpdate(entity.getBlockPos(), entity.getBlockState().setValue(AbstractPotBlock.LID, PotState.WITHOUT_LID));
        }
        entity.itemStackHandlerChanged();
        return result;
    }

    public boolean isEmpty() {
        return storedFluid.isEmpty();
    }

    public boolean isFull() {
        return getSpace() == 0;
    }

    public boolean isWaterOrSoup() {
        return isWater() || isSoup();
    }

    public boolean isWater() {
        return storedFluid.is(CTag.WATER);
    }

    public boolean isSoup() {
        return storedFluid.is(CTag.SOUP);
    }

    public int getSpace() {
        return capacity - storedFluid.getAmount();
    }

    public boolean canTakeOut() {
        return storedFluid.getAmount() >= FluidType.BUCKET_VOLUME;
    }

    public FluidStack getStoredFluid() {
        return storedFluid;
    }

    public int getCapacity() {
        return capacity;
    }

    public AbstractPotBlockEntity getEntity() {
        return entity;
    }

    public void setStoredFluidDirectly(FluidStack fluidStack) {
        this.storedFluid = fluidStack.copy();
        entity.itemStackHandlerChanged();
    }
}
