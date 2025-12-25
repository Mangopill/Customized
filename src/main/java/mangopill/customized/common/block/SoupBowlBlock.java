package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.record.PlateRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

import javax.annotation.Nullable;

public class SoupBowlBlock extends AbstractPlateBlock {
    public static final MapCodec<SoupBowlBlock> CODEC = simpleCodec(SoupBowlBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_DRIVE = Shapes.or(
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D),
            Block.box(3.0D, 1.0D, 4.0D, 4.0D, 3.0D, 12.0D),
            Block.box(3.0D, 1.0D, 12.0D, 13.0D, 3.0D, 13.0D),
            Block.box(3.0D, 3.0D, 2.0D, 13.0D, 6.0D, 3.0D),
            Block.box(2.0D, 3.0D, 3.0D, 3.0D, 6.0D, 13.0D),
            Block.box(13.0D, 3.0D, 3.0D, 14.0D, 6.0D, 13.0D),
            Block.box(3.0D, 3.0D, 13.0D, 13.0D, 6.0D, 14.0D),
            Block.box(3.0D, 1.0D, 3.0D, 13.0D, 3.0D, 4.0D),
            Block.box(12.0D, 1.0D, 4.0D, 13.0D, 3.0D, 12.0D)
    );

    public SoupBowlBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PlateRecord.SOUP_BOWL.type().get().create(pos, state);
    }

    @Override
    public VoxelShape setShapeWithoutDrive() {
        return BLOCK_SHAPE_WITHOUT_DRIVE;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE_WITHOUT_DRIVE;
    }
}
