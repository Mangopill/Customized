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
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(1.0D, 2.0D, 2.0D, 2.0D, 6.0D, 14.0D),
            Block.box(14.0D, 2.0D, 2.0D, 15.0D, 6.0D, 14.0D),
            Block.box(2.0D, 2.0D, 1.0D, 14.0D, 6.0D, 2.0D),
            Block.box(2.0D, 2.0D, 14.0D, 14.0D, 6.0D, 15.0D),
            Block.box(15.0D, 6.0D, 2.0D, 16.0D, 7.0D, 14.0D),
            Block.box(0.0D, 6.0D, 2.0D, 1.0D, 7.0D, 14.0D),
            Block.box(2.0D, 6.0D, 0.0D, 14.0D, 7.0D, 1.0D),
            Block.box(2.0D, 6.0D, 15.0D, 14.0D, 7.0D, 16.0D),
            Block.box(1.0D, 6.0D, 2.0D, 2.0D, 11.0D, 14.0D),
            Block.box(14.0D, 6.0D, 2.0D, 15.0D, 11.0D, 14.0D),
            Block.box(2.0D, 6.0D, 1.0D, 14.0D, 11.0D, 2.0D),
            Block.box(2.0D, 6.0D, 14.0D, 14.0D, 11.0D, 15.0D),
            Block.box(1.95D, 5.0D, 1.95D, 13.95D, 6.0D, 13.95D)
    );
    protected static final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(
            BLOCK_SHAPE_WITHOUT_LID,
            Block.box(2.0D, 11.05D, 2.0D, 14.0D, 11.55D, 14.0D),
            Block.box(4.0D, 11.05D, 2.0D, 12.0D, 11.55D, 4.0D),
            Block.box(4.0D, 11.05D, 12.0D, 12.0D, 11.55D, 14),
            Block.box(2.0D, 11.05D, 2.0D, 4.0D, 11.55D, 14.0D),
            Block.box(12.0D, 11.05D, 2.0D, 14.0D, 11.55D, 14.0D),
            Block.box(4.0D, 11.55D, 4.0D, 12.0D, 12.05D, 12.0D),
            Block.box(14.5D, 11.0D, 2.0D, 15.5D, 12.0D, 14.0D),
            Block.box(0.5D, 11.0D, 2.0D, 1.5D, 12.0D, 14.0D),
            Block.box(2.0D, 11.0D, 0.5D, 14.0D, 12.0D, 1.5D),
            Block.box(2.0D, 11.0D, 14.5D, 14.0D, 12.0D, 15.5D),
            Block.box(14.0D, 11.0D, 1.0D, 15.0D, 12.0D, 2.0D),
            Block.box(14.0D, 11.0D, 14.0D, 15.0D, 12.0D, 15.0D),
            Block.box(1.0D, 11.0D, 1.0D, 2.0D, 12.0D, 2.0D),
            Block.box(1.0D, 11.0D, 14.0D, 2.0D, 12.0D, 15.0D)
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
