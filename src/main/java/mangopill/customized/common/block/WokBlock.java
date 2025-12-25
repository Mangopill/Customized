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

public class WokBlock extends AbstractPotBlock{
    public static final MapCodec<WokBlock> CODEC = simpleCodec(WokBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_LID = Shapes.or(
            Block.box(13.0D, 1.0D, 3.0D, 14.0D, 3.0D, 13.0D)
    );
    protected static final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(
            BLOCK_SHAPE_WITHOUT_LID,
            Block.box(12.0D, 8.0D, 4.0D, 14.0D, 8.5D, 12.0D)
    );

    public WokBlock(Properties properties) {
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
        return PotRecord.WOK.entityType();
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
