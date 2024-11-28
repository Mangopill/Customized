package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.SoupBowlBlockEntity;
import mangopill.customized.common.block.record.PlateRecord;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SoupBowlBlock extends AbstractPlateBlock {
    public static final MapCodec<SoupBowlBlock> CODEC = simpleCodec(SoupBowlBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_DRIVE = Shapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(3, 1, 4, 4, 3, 12),
            Block.box(3, 1, 12, 13, 3, 13),
            Block.box(3, 3, 2, 13, 6, 3),
            Block.box(2, 3, 3, 3, 6, 13),
            Block.box(13, 3, 3, 14, 6, 13),
            Block.box(3, 3, 13, 13, 6, 14),
            Block.box(3, 1, 3, 13, 3, 4),
            Block.box(12, 1, 4, 13, 3, 12)
    );

    public SoupBowlBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SoupBowlBlockEntity) {
            if (random.nextFloat() <= 0.3F) {
                double x = (double) pos.getX() + 0.2D + (random.nextDouble() * 0.3D);
                double y = (double) pos.getY() + 0.2D;
                double z = (double) pos.getZ() + 0.2D + (random.nextDouble() * 0.3D);
                level.addParticle(ModParticleTypeRegistry.AROMA.get(), x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return PlateRecord.SOUP_BOWL.type().create(pos, state);
    }

    @Override
    public VoxelShape setShapeWithoutDrive() {
        return BLOCK_SHAPE_WITHOUT_DRIVE;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE_WITHOUT_DRIVE;
    }
}
