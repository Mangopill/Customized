package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
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
import org.jetbrains.annotations.NotNull;

public class GingerCropBlock extends CropBlock {
    public static final MapCodec<GingerCropBlock> CODEC = simpleCodec(GingerCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 10.0, 14.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 10.0, 14.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 13.0, 14.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 13.0, 14.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 13.0, 14.0),
            Block.box(4.0, 0.0, 4.0, 14.0, 14.0, 14.0),
    };

    @Override
    public @NotNull MapCodec<GingerCropBlock> codec() {
        return CODEC;
    }

    public GingerCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModItemRegistry.GINGER.get();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}