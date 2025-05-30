package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.PotStrategyHandler;
import net.minecraft.core.*;
import net.minecraft.world.*;
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

import javax.annotation.Nonnull;

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
    public @Nonnull InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        return PotStrategyHandler.getInstance().useByRegistry(this.getDescriptionId(), player.getItemInHand(hand), state, level, pos, player, hand, result);
    }

    abstract public VoxelShape setShapeWithoutLid();

    abstract public VoxelShape setShapeWithLid();

    abstract public VoxelShape setShapeWithDrive();

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(LID);
    }

    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nonnull VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return switch (state.getValue(LID)){
            case WITHOUT_LID -> setShapeWithoutLid();
            case WITH_LID -> setShapeWithLid();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public @Nonnull VoxelShape getCollisionShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
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
    public @Nonnull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nonnull BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level,
                            @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() == newState.getBlock()) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof AbstractPotBlockEntity potBlockEntity) {
            NonNullList<ItemStack> stackNonNullList = NonNullList.create();
            stackNonNullList.addAll(potBlockEntity.getItemStackListInPot(false, true));
            Containers.dropContents(level, pos, stackNonNullList);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
