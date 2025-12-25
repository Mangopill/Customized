package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class ChilliCropBlock extends CropBlock {
    public static final MapCodec<ChilliCropBlock> CODEC = simpleCodec(ChilliCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 13.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D),
    };

    @Override
    public MapCodec<ChilliCropBlock> codec() {
        return CODEC;
    }

    public ChilliCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CItemRegistry.CHILLI_SEED.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
