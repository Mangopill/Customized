package mangopill.customized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RiceCropBlock extends TallWaterloggedCropBlock {
    public RiceCropBlock(Properties properties) {
        super(properties, BlockStateProperties.MAX_AGE_3);
    }

    @Override
    public VoxelShape[] setShapeByAge() {
        return new VoxelShape[]{
                Block.box(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D),
                Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D),
                Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D),
                Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D),
        };
    }

    @Override
    public int setGrowChance(BlockState blockState, BlockGetter getter, BlockPos pos) {
        return 3;
    }
}
