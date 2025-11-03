package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SichuanPepperCropBlock extends CropBlock {
    public static final MapCodec<SichuanPepperCropBlock> CODEC = simpleCodec(SichuanPepperCropBlock::new);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0),
    };

    @Override
    public MapCodec<SichuanPepperCropBlock> codec() {
        return CODEC;
    }

    public SichuanPepperCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CItemRegistry.SICHUAN_PEPPER.get();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}