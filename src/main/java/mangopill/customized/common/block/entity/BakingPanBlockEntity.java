package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PlateRegistryRecord;
import mangopill.customized.common.block.record.PlateSlotRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BakingPanBlockEntity extends AbstractPlateBlockEntity {
    public BakingPanBlockEntity(BlockPos pos, BlockState blockState) {
        super(PlateRegistryRecord.BAKING_PAN.type(), pos, blockState, PlateSlotRecord.BAKING_PAN.ingredientInput(),
                PlateSlotRecord.BAKING_PAN.seasoningInput(), PlateSlotRecord.BAKING_PAN.spiceInput());
    }
}
