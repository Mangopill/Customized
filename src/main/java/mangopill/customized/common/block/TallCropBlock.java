package mangopill.customized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

public abstract class TallCropBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final BooleanProperty GROW_PLACED = BooleanProperty.create("grow_placed");
    private final int topMaxAge = BlockStateProperties.MAX_AGE_7;
    private final int bottomMaxAge;

    public TallCropBlock(Properties properties, int bottomMaxAge) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0)
                .setValue(BlockStateProperties.WATERLOGGED, true)
                .setValue(GROW_PLACED, false));
        this.bottomMaxAge = bottomMaxAge;
    }

    abstract public VoxelShape[] setShapeByAge();

    abstract public int setGrowChance();

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level,
                           @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, level, pos, random);
        if (level.getRawBrightness(pos.above(), 0) < 6 || !level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!CommonHooks.canCropGrow(level, pos, state, random.nextInt(setGrowChance()) == 0)) {
            return;
        }
        if (isTop(state)) {
            int topAge = state.getValue(AGE);
            if (topAge >= topMaxAge){
                return;
            }
            level.setBlockAndUpdate(pos, state.setValue(AGE, topAge + 1));
            CommonHooks.fireCropGrowPost(level, pos, state);
        } else {
            int age = state.getValue(AGE);
            if (age >= bottomMaxAge){
                if (!level.isEmptyBlock(pos.above())) {
                    return;
                }
                level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(AGE, bottomMaxAge + 1).setValue(BlockStateProperties.WATERLOGGED, false).setValue(GROW_PLACED, true));
                return;
            }
            level.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
            CommonHooks.fireCropGrowPost(level, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos,
                                         @NotNull BlockState blockState) {
        return blockState.getValue(AGE) < topMaxAge;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource randomSource,
                                     @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource,
                                @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        int bottomAge = blockState.getValue(AGE);
        int newBottomAge = Math.min(bottomAge + Mth.nextInt(randomSource, 1, 3), bottomMaxAge);
        if (!isTop(blockState) && bottomAge < bottomMaxAge) {
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, newBottomAge));
        } else {
            if (isTop(blockState)) {
                int topAge = blockState.getValue(AGE);
                int newTopAge = Math.min(topAge + Mth.nextInt(randomSource, 1, 3), topMaxAge);
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, newTopAge));
            } else {
                BlockPos posAbove = blockPos.above();
                BlockState stateAbove = serverLevel.getBlockState(posAbove);
                if (serverLevel.isEmptyBlock(posAbove)) {
                    serverLevel.setBlockAndUpdate(posAbove, this.defaultBlockState().setValue(AGE, bottomMaxAge + 1).setValue(BlockStateProperties.WATERLOGGED, false).setValue(GROW_PLACED, true));
                } else if (stateAbove.getBlock() == this) {
                    int newTopAge = Math.min(Math.max(stateAbove.getValue(AGE), bottomMaxAge) + Mth.nextInt(randomSource, 1, topMaxAge - bottomMaxAge), topMaxAge);
                    serverLevel.setBlockAndUpdate(posAbove, stateAbove.setValue(AGE, newTopAge));
                }
            }
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return setShapeByAge()[state.getValue(AGE)];
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, @NotNull BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.getBlock() instanceof TallCropBlock && state.getValue(GROW_PLACED)) {
            return !isTop(stateBelow);
        }
        return super.canSurvive(state, level, pos) && fluid.is(FluidTags.WATER) && fluid.isSource();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE, BlockStateProperties.WATERLOGGED, GROW_PLACED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public boolean isTop(BlockState state) {
        return state.getValue(AGE) > bottomMaxAge;
    }
}
