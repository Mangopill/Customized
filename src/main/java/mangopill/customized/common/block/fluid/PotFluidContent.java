package mangopill.customized.common.block.fluid;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.ModBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidType;

public class PotFluidContent {
    private final Block block;
    private final Fluid fluid;
    private final int totalAmount;
    private final PotState enumProperty;

    protected PotFluidContent(Block block, Fluid fluid, int totalAmount, PotState enumProperty) {
        this.block = block;
        this.fluid = fluid;
        this.totalAmount = totalAmount;
        this.enumProperty = enumProperty;
    }

    public static PotFluidContent getContent(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID)){
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.WATER, FluidType.BUCKET_VOLUME, PotState.WITH_DRIVE);
        } else {
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.EMPTY, 0, PotState.WITH_DRIVE);
        }
    }

    public static PotFluidContent getContentForFill(Level level, BlockPos pos) {
        if (level.getBlockState(pos).getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID)){
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.WATER, FluidType.BUCKET_VOLUME, PotState.WITH_DRIVE);
        } else {
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.EMPTY, 0, PotState.WITH_DRIVE);
        }
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

    public PotState getEnumProperty() {
        return enumProperty;
    }
}
