package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class ScallionCropBlock extends CropBlock {
    public static final MapCodec<ScallionCropBlock> CODEC = simpleCodec(ScallionCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D),
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D),
    };

    @Override
    public MapCodec<ScallionCropBlock> codec() {
        return CODEC;
    }

    public ScallionCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CItemRegistry.SCALLION.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}