package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PlateRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlatterBlockEntity extends AbstractPlateBlockEntity {
    public PlatterBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, PlateRecord.PLATTER);
    }
}
