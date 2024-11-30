package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PlateRegistryRecord;
import mangopill.customized.common.block.record.PlateSlotRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupBowlBlockEntity extends AbstractPlateBlockEntity {
    public SoupBowlBlockEntity(BlockPos pos, BlockState blockState) {
        super(PlateRegistryRecord.SOUP_BOWL.type(), pos, blockState, PlateSlotRecord.SOUP_BOWL.ingredientInput(), PlateSlotRecord.SOUP_BOWL.seasoningInput());
    }
}
