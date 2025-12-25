package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class BitterGourdCropBlock extends CropBlock {
    public static final MapCodec<BitterGourdCropBlock> CODEC = simpleCodec(BitterGourdCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D),
    };

    @Override
    public MapCodec<BitterGourdCropBlock> codec() {
        return CODEC;
    }

    public BitterGourdCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CItemRegistry.BITTER_GOURD_SEED.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
