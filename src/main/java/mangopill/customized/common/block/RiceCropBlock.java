package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RiceCropBlock extends TallWaterloggedCropBlock {
    public static final MapCodec<RiceCropBlock> CODEC = simpleCodec(RiceCropBlock::new);
    public RiceCropBlock(Properties properties) {
        super(properties, BlockStateProperties.MAX_AGE_3);
    }

    @Override
    public VoxelShape[] setShapeByAge() {
        return new VoxelShape[]{
                Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D),
                Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D),
                Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
                Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D),
        };
    }

    @Override
    public int setGrowChance() {
        return 3;
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
