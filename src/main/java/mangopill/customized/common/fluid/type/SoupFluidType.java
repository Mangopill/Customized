package mangopill.customized.common.fluid.type;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

import static mangopill.customized.common.util.component.FluidComponentUtil.*;

public class SoupFluidType extends CFluidType implements TintedFluidType {

    public SoupFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

    @Override
    public int getTintColor() {
        return 0xCC3F76E4;
    }

    @Override
    public int getTintColor(FluidStack stack) {
        return getColor(stack) == NO_TINT ? 0xCC3F76E4 : getColor(stack);
    }

    @Override
    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return 0xCC3F76E4;
    }
}
