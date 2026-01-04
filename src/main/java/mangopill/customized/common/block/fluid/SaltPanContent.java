package mangopill.customized.common.block.fluid;

import mangopill.customized.common.block.SaltPanBlock;
import mangopill.customized.common.registry.CBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.*;

public class SaltPanContent extends CFluidContent<Boolean, Boolean> {

    protected SaltPanContent(Block block, Fluid fluid, int totalAmount, Boolean withWater) {
        super(block, fluid, totalAmount, SaltPanBlock.WITH_WATER, withWater);
    }

    public static SaltPanContent getContent(Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(SaltPanBlock.WITH_WATER).equals(true)
                ? new SaltPanContent(CBlockRegistry.SALT_PAN.get(), Fluids.WATER, 333, false)
                : new SaltPanContent(CBlockRegistry.SALT_PAN.get(), Fluids.EMPTY, 0, false);
    }

    public static SaltPanContent getContentForFill(Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(SaltPanBlock.WITH_WATER).equals(false)
                ? new SaltPanContent(CBlockRegistry.SALT_PAN.get(), Fluids.WATER, 333, true)
                : new SaltPanContent(CBlockRegistry.SALT_PAN.get(), Fluids.EMPTY, 0, true);
    }
}
