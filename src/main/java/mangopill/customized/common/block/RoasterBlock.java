package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.SensoryUtil.*;


public class RoasterBlock extends AbstractPotBlock {
    public static final MapCodec<RoasterBlock> CODEC = simpleCodec(RoasterBlock::new);

    protected static final VoxelShape BLOCK_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    );

    public RoasterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (state.getValue(LID).equals(PotState.WITHOUT_LID) || !(entity instanceof LivingEntity)) return;
        entity.hurt(level.damageSources().hotFloor(), 1.0F);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!ROASTER_SOUND.get() || state.getValue(LID).equals(PotState.WITHOUT_LID)) return;
        playRandomSound(level, null, pos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, random, 0.1F, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F);
    }

    @Override
    public BlockEntityType<? extends AbstractPotBlockEntity> setBlockEntity() {
        return PotRecord.ROASTER.entityType();
    }

    @Override
    public VoxelShape setShapeWithoutLid() {
        return BLOCK_SHAPE;
    }

    @Override
    public VoxelShape setShapeWithLid() {
        return BLOCK_SHAPE;
    }

    @Override
    public VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE;
    }
}
