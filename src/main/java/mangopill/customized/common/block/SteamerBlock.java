package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.record.PotRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

public class SteamerBlock extends AbstractPotBlock{
    public static final MapCodec<SteamerBlock> CODEC = simpleCodec(SteamerBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_LID = Shapes.or(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(1, 2, 2, 2, 6, 14),
            Block.box(14, 2, 2, 15, 6, 14),
            Block.box(2, 2, 1, 14, 6, 2),
            Block.box(2, 2, 14, 14, 6, 15),
            Block.box(15, 6, 2, 16, 7, 14),
            Block.box(0, 6, 2, 1, 7, 14),
            Block.box(2, 6, 0, 14, 7, 1),
            Block.box(2, 6, 15, 14, 7, 16),
            Block.box(1, 6, 2, 2, 11, 14),
            Block.box(14, 6, 2, 15, 11, 14),
            Block.box(2, 6, 1, 14, 11, 2),
            Block.box(2, 6, 14, 14, 11, 15),
            Block.box(1.95, 5, 1.95, 13.95, 6, 13.95)
    );
    protected static final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(
            BLOCK_SHAPE_WITHOUT_LID,
            Block.box(2, 11.05, 2, 14, 11.55, 14),
            Block.box(4, 11.05, 2, 12, 11.55, 4),
            Block.box(4, 11.05, 12, 12, 11.55, 14),
            Block.box(2, 11.05, 2, 4, 11.55, 14),
            Block.box(12, 11.05, 2, 14, 11.55, 14),
            Block.box(4, 11.55, 4, 12, 12.05, 12),
            Block.box(14.5, 11, 2, 15.5, 12, 14),
            Block.box(0.5, 11, 2, 1.5, 12, 14),
            Block.box(2, 11, 0.5, 14, 12, 1.5),
            Block.box(2, 11, 14.5, 14, 12, 15.5),
            Block.box(14, 11, 1, 15, 12, 2),
            Block.box(14, 11, 14, 15, 12, 15),
            Block.box(1, 11, 1, 2, 12, 2),
            Block.box(1, 11, 14, 2, 12, 15)
    );

    public SteamerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public BlockEntityType<? extends AbstractPotBlockEntity> setBlockEntity() {
        return PotRecord.STEAMER.entityType();
    }

    @Override
    public VoxelShape setShapeWithoutLid() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }

    @Override
    public VoxelShape setShapeWithLid() {
        return BLOCK_SHAPE_WITH_LID;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }
}
