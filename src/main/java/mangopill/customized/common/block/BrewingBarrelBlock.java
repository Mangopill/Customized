package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.Nonnull;

public class BrewingBarrelBlock extends BaseEntityBlock {
    public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, 12);

    public BrewingBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(PROGRESS, 0)
        );
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (level.isClientSide){
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BrewingBarrelBlockEntity barrelBlockEntity) {
            barrelBlockEntity.interact(player.getItemInHand(hand), player, level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level,
                            @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() == newState.getBlock()) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof BrewingBarrelBlockEntity brewingBarrelBlockEntity) {
            NonNullList<ItemStack> stackNonNullList = NonNullList.create();
            stackNonNullList.addAll(brewingBarrelBlockEntity.getItemStackListInBrewingBarrel(false));
            Containers.dropContents(level, pos, stackNonNullList);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntityTypeRegistry.BREWING_BARREL.get(), BrewingBarrelBlockEntity::cookingTick);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(PROGRESS);
    }

    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(PROGRESS, 0);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return ModBlockEntityTypeRegistry.BREWING_BARREL.get().create(blockPos, blockState);
    }
}
