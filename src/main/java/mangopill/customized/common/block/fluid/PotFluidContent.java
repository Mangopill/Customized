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

public class PotFluidContent extends ModFluidContent<PotState> {

    protected PotFluidContent(Block block, Fluid fluid, int totalAmount, PotState enumProperty) {
        super(block, fluid, totalAmount, enumProperty);
    }

    public static PotFluidContent getContent(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID)){
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.WATER, FluidType.BUCKET_VOLUME, PotState.WITHOUT_LID);
        } else {
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.EMPTY, 0, null);
        }
    }

    public static PotFluidContent getContentForFill(Level level, BlockPos pos) {
        if (level.getBlockState(pos).getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID)){
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.WATER, FluidType.BUCKET_VOLUME, PotState.WITH_DRIVE);
        } else {
            return new PotFluidContent(ModBlockRegistry.CASSEROLE.get(), Fluids.EMPTY, 0, null);
        }
    }
}
