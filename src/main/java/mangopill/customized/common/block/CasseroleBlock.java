package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.CSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

import static mangopill.customized.common.CustomizedConfig.*;

public class CasseroleBlock extends AbstractPotBlock{
    public static final MapCodec<CasseroleBlock> CODEC = simpleCodec(CasseroleBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_LID = Shapes.or(
            Block.box(13.0D, 1.0D, 3.0D, 14.0D, 3.0D, 13.0D),
            Block.box(1.0D, 3.0D, 2.0D, 2.0D, 8.0D, 14.0D),
            Block.box(0.5D, 8.0D, 2.0D, 1.5D, 9.0D, 14.0D),
            Block.box(1.0D, 8.0D, 1.0D, 2.0D, 9.0D, 2.0D),
            Block.box(1.0D, 8.0D, 14.0D, 2.0D, 9.0D, 15.0D),
            Block.box(14.0D, 8.0D, 14.0D, 15.0D, 9.0D, 15.0D),
            Block.box(14.0D, 8.0D, 1.0D, 15.0D, 9.0D, 2.0D),
            Block.box(14.5D, 8.0D, 2.0D, 15.5D, 9.0D, 14.0D),
            Block.box(14.0D, 3.0D, 2.0D, 15.0D, 8.0D, 14.0D),
            Block.box(2.0D, 8.0D, 0.5D, 14.0D, 9.0D, 1.5D),
            Block.box(2.0D, 8.0D, 14.5D, 14.0D, 9.0D, 15.5D),
            Block.box(3.0D, 1.0D, 2.0D, 13.0D, 3.0D, 3.0D),
            Block.box(2.0D, 3.0D, 1.0D, 14.0D, 8.0D, 2.0D),
            Block.box(2.0D, 3.0D, 14.0D, 14.0D, 8.0D, 15.0D),
            Block.box(3.0D, 1.0D, 13.0D, 13.0D, 3.0D, 14.0D),
            Block.box(2.0D, 1.0D, 3.0D, 3.0D, 3.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D),
            Block.box(2.0D, 2.0D, 2.0D, 3.0D, 3.0D, 3.0D),
            Block.box(2.0D, 2.0D, 13.0D, 3.0D, 3.0D, 14.0D),
            Block.box(13.0D, 2.0D, 13.0D, 14.0D, 3.0D, 14.0D),
            Block.box(13.0D, 2.0D, 2.0D, 14.0D, 3.0D, 3.0D)
    );
    protected static final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(
            BLOCK_SHAPE_WITHOUT_LID,
            Block.box(12.0D, 8.0D, 4.0D, 14.0D, 8.5D, 12.0D),
            Block.box(2.0D, 8.0D, 2.0D, 14.0D, 8.5D, 4.0D),
            Block.box(4.0D, 8.5D, 4.0D, 12.0D, 9.0D, 12.0D),
            Block.box(2.0D, 8.0D, 4.0D, 4.0D, 8.5D, 12.0D),
            Block.box(2.0D, 8.0D, 12.0D, 14.0D, 8.5D, 14.0D),
            Block.box(6.5D, 9.5D, 6.5D, 9.5D, 10.0D, 9.5D),
            Block.box(7.0D, 9.5D, 7.0D, 9.0D, 10.5D, 9.0D),
            Block.box(7.0D, 9.0D, 7.0D, 9.0D, 9.5D, 9.0D)
    );

    public CasseroleBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CasseroleBlockEntity cookingPotEntity && cookingPotEntity.isHeated() && !state.getValue(LID).equals(PotState.WITHOUT_LID)) {
            double x = (double) pos.getX() + 0.4D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.4D;
            if (random.nextInt(8) == 0 && CASSEROLE_SOUND.get()) {
                SoundEvent sound = state.getValue(LID).equals(PotState.WITH_LID)
                        ? CSoundRegistry.BOILING_WATER_WITH_LID.get()
                        : CSoundRegistry.BOILING_WATER_WITHOUT_LID.get();
                level.playLocalSound(x, y, z, sound, SoundSource.BLOCKS, Math.clamp(random.nextFloat() + 0.01F, 0.01F, 0.3F), 1.0F, false);
            }
        }
    }

    @Override
    public BlockEntityType<? extends AbstractPotBlockEntity> setBlockEntity() {
        return PotRecord.CASSEROLE.entityType();
    }

    @Override
    public VoxelShape setShapeWithoutLid() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }

    @Override
    public VoxelShape setShapeWithLid() {
        return BLOCK_SHAPE_WITH_LID;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }
}
