package mangopill.customized.common.block.fluid;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;

public abstract class ModFluidContent<T extends Comparable<T>, V extends T> {
    private final Block block;
    private final Fluid fluid;
    private final int totalAmount;
    private final Property<T> property;
    private final V value;

    protected ModFluidContent(Block block, Fluid fluid, int totalAmount, Property<T> property, V value) {
        this.block = block;
        this.fluid = fluid;
        this.totalAmount = totalAmount;
        this.property = property;
        this.value = value;
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

    public Property<T> getProperty() {
        return property;
    }

    public V getValue() {
        return value;
    }
}
