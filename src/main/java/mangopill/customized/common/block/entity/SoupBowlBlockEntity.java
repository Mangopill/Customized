package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PlateRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupBowlBlockEntity extends AbstractPlateBlockEntity {
    public SoupBowlBlockEntity(BlockPos pos, BlockState blockState) {
        super(PlateRecord.SOUP_BOWL.type(), pos, blockState, PlateRecord.SOUP_BOWL.ingredientInput(), PlateRecord.SOUP_BOWL.seasoningInput());
    }
}
