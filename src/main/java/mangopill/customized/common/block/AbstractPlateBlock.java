package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

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
    public @Nonnull InteractionResult use(@Nonnull BlockState state,
                                             @Nonnull Level level, @Nonnull BlockPos pos,
                                             @Nonnull Player player, @Nonnull InteractionHand hand,
                                             @Nonnull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPlateBlockEntity plateBlockEntity){
            plateBlockEntity.eatFood(level, player, state, pos);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(DRIVE);
    }

    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nonnull VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return switch (state.getValue(DRIVE)){
            case WITHOUT_DRIVE -> setShapeWithoutDrive();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public @Nonnull VoxelShape getCollisionShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
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
    public void animateTick(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
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
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateEntity) {
            plateEntity.setChanged();
        }
    }

    @Override
    public @Nonnull ItemStack getCloneItemStack(@Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        return level.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateBlockEntity
                ? plateBlockEntity.getCloneItemStack(stack) : stack;
    }

    @Override
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof AbstractPlateBlockEntity plateEntity) {
            for (ItemStack stack : drops) {
                if (stack.getItem() instanceof AbstractPlateItem) {
                    stack.setTag(plateEntity.getUpdateTag());
                }
            }
        }
        return drops;
    }
}
