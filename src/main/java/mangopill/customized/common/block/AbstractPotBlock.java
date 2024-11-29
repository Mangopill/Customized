package mangopill.customized.common.block;

import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotStrategyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import static mangopill.customized.common.block.state.PotState.*;

public abstract class AbstractPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<PotState> LID = EnumProperty.create("lid", PotState.class);

    protected AbstractPotBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(LID, WITHOUT_LID)
        );
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack itemStackInHand, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        return PotStrategyHandler.getInstance().useByRegistry(this.getDescriptionId(), itemStackInHand, state, level, pos, player, hand, result);
    }

    abstract public VoxelShape setShapeWithoutLid();

    abstract public VoxelShape setShapeWithLid();

    abstract public VoxelShape setShapeWithDrive();

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
            case WITHOUT_LID -> setShapeWithoutLid();
            case WITH_LID -> setShapeWithLid();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(LID)){
            case WITHOUT_LID -> setShapeWithoutLid();
            case WITH_LID -> setShapeWithLid();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean hasWater = context.getLevel().getFluidState(context.getClickedPos()).getType().isSame(Fluids.WATER);
        return defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, hasWater)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(LID, WITHOUT_LID);
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
}
