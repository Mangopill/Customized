package mangopill.customized.common.block;

import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPotBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final EnumProperty<PotState> LID = EnumProperty.create("lid", PotState.class);
    protected static VoxelShape SHAPE_WITHOUT_LID;
    protected static VoxelShape SHAPE_WITH_LID;
    protected static VoxelShape SHAPE_WITH_WATER;

    protected AbstractPotBlock(Properties properties, VoxelShape withoutIld, VoxelShape withIld, VoxelShape withWater) {
        super(properties);
        SHAPE_WITHOUT_LID = withoutIld;
        SHAPE_WITH_LID = withIld;
        SHAPE_WITH_WATER = withWater;
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(LID, PotState.WITHOUT_LID)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(LID);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(LID)){
            case WITHOUT_LID -> SHAPE_WITHOUT_LID;
            case WITH_LID -> SHAPE_WITH_LID;
            case WITH_WATER -> SHAPE_WITH_WATER;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(LID)){
            case WITHOUT_LID -> SHAPE_WITHOUT_LID;
            case WITH_LID -> SHAPE_WITH_LID;
            case WITH_WATER -> SHAPE_WITH_WATER;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean hasWater = context.getLevel().getFluidState(context.getClickedPos()).getType().isSame(Fluids.WATER);
        return defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, hasWater)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(LID, PotState.WITHOUT_LID);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (facing == Direction.DOWN && !this.canSurvive(state, level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
