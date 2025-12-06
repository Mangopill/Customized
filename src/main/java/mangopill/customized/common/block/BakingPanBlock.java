package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.record.PlateRegistryRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BakingPanBlock extends AbstractPlateBlock {
    public static final MapCodec<BakingPanBlock> CODEC = simpleCodec(BakingPanBlock::new);

    protected static final VoxelShape BLOCK_SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 1, 12)
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
        return PlateRegistryRecord.BAKING_PAN.type().create(pos, state);
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
