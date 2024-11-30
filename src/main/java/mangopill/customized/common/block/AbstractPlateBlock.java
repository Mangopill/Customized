package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
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

import static mangopill.customized.common.block.state.PlateState.*;

public abstract class AbstractPlateBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<PlateState> DRIVE = EnumProperty.create("drive", PlateState.class);

    protected AbstractPlateBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(DRIVE, WITHOUT_DRIVE)
        );
    }

    abstract public VoxelShape setShapeWithoutDrive();

    abstract public VoxelShape setShapeWithDrive();

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                       @NotNull Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPlateBlockEntity plateBlockEntity){
            plateBlockEntity.eatFood(stack, level, player, state, pos);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(DRIVE);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(DRIVE)){
            case WITHOUT_DRIVE -> setShapeWithoutDrive();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(DRIVE)){
            case WITHOUT_DRIVE -> setShapeWithoutDrive();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean hasWater = context.getLevel().getFluidState(context.getClickedPos()).getType().isSame(Fluids.WATER);
        return defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, hasWater)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(DRIVE, WITHOUT_DRIVE);
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

    @Override
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPlateBlockEntity && state.getValue(DRIVE).equals(WITH_DRIVE)) {
            if (random.nextFloat() <= 0.3F) {
                double x = (double) pos.getX() + 0.2D + (random.nextDouble() * 0.3D);
                double y = (double) pos.getY() + 0.2D;
                double z = (double) pos.getZ() + 0.2D + (random.nextDouble() * 0.3D);
                level.addParticle(ModParticleTypeRegistry.AROMA.get(), x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateBlockEntity){
            stack = plateBlockEntity.getCloneItemStack(stack);
        }
        return stack;
    }
}
