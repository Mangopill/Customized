package mangopill.customized.common.block;

import mangopill.customized.common.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class BroadBeanCropBlock extends CropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0),
    };

    public BroadBeanCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @Nonnull ItemLike getBaseSeedId() {
        return ModItemRegistry.BROAD_BEAN.get();
    }

    @Override
    public @Nonnull VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level,
                                           @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
