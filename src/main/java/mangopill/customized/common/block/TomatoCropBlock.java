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

public class TomatoCropBlock extends CropBlock{
    public static final MapCodec<TomatoCropBlock> CODEC = simpleCodec(TomatoCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0),
            Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0),
            Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)
    };

    @Override
    public @NotNull MapCodec<TomatoCropBlock> codec() {
        return CODEC;
    }

    public TomatoCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModItemRegistry.TOMATO_SEED.get();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
