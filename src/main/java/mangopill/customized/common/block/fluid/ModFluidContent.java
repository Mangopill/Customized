package mangopill.customized.common.block.fluid;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public abstract class ModFluidContent<T extends Comparable<T>> {
    private final Block block;
    private final Fluid fluid;
    private final int totalAmount;
    private final T property;

    protected ModFluidContent(Block block, Fluid fluid, int totalAmount, T property) {
        this.block = block;
        this.fluid = fluid;
        this.totalAmount = totalAmount;
        this.property = property;
    }

    public Block getBlock() {
        return block;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public T getProperty() {
        return property;
    }
}
