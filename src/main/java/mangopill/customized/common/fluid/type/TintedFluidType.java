package mangopill.customized.common.fluid.type;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

public interface TintedFluidType {
    int getTintColor();
    int getTintColor(FluidStack stack);
    int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);
}
