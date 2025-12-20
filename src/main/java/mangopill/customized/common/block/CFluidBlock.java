package mangopill.customized.common.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.function.Supplier;

public class CFluidBlock extends LiquidBlock {
    public CFluidBlock(Supplier<BaseFlowingFluid> fluid, Properties properties) {
        super(fluid.get(), properties);
    }
}
