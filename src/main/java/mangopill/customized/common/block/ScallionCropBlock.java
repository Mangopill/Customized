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

public class ScallionCropBlock extends CropBlock {
    public static final MapCodec<ScallionCropBlock> CODEC = simpleCodec(ScallionCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 11.0, 9.0),
    };

    @Override
    public @NotNull MapCodec<ScallionCropBlock> codec() {
        return CODEC;
    }

    public ScallionCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModItemRegistry.SCALLION.get();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}