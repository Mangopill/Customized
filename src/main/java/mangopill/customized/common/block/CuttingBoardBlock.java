package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.CuttingBoardBlockEntity;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.minecraft.core.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlock extends BaseEntityBlock implements CSimpleInteractableBlock {
    public static final MapCodec<CuttingBoardBlock> CODEC = simpleCodec(CuttingBoardBlock::new);

    protected static final VoxelShape X_SHAPE = Shapes.or(
            Block.box(1, 0, 4, 15, 1, 12)
    );
    protected static final VoxelShape Z_SHAPE = Shapes.or(
            Block.box(4, 0, 1, 12, 1, 15)
    );

    public CuttingBoardBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult result) {
        return simpleInteract(itemStackInHand, state, level, pos, player, hand, SoundEvents.WOOD_HIT, SoundEvents.WOOD_PLACE);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() == newState.getBlock()) return;
        if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
            NonNullList<ItemStack> stackNonNullList = NonNullList.create();
            stackNonNullList.addAll(cuttingBoardBlockEntity.getItemStackListInBlockEntity(true));
            Containers.dropContents(level, pos, stackNonNullList);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return  switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case WEST, EAST -> Z_SHAPE;
            default -> X_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case WEST, EAST -> Z_SHAPE;
            default -> X_SHAPE;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return CBlockEntityTypeRegistry.CUTTING_BOARD.get().create(blockPos, blockState);
    }
}
