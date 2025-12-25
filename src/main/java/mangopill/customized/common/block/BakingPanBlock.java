package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.record.PlateRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

import javax.annotation.Nullable;

public class BakingPanBlock extends AbstractPlateBlock {
    public static final MapCodec<BakingPanBlock> CODEC = simpleCodec(BakingPanBlock::new);

    protected static final VoxelShape BLOCK_SHAPE = Shapes.or(
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D)
    );

    public BakingPanBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PlateRecord.BAKING_PAN.type().get().create(pos, state);
    }

    @Override
    public VoxelShape setShapeWithoutDrive() {
        return BLOCK_SHAPE;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE;
    }
}
