package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.block.strategy.base.*;
import mangopill.customized.common.block.strategy.pot.LidStrategy;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.common.block.state.PotState.*;

public abstract class AbstractPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, CSimpleDropContentsBlock {
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
    public ItemInteractionResult useItemOn(
            ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult result) {
        return PotStrategyHandler.getInstance().useByRegistry(getDescriptionId(), itemStackInHand, state, level, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!state.getValue(LID).equals(PotState.WITHOUT_LID)) {
            return level.isClientSide ? createTickerHelper(blockEntityType, setBlockEntity(), AbstractPotBlockEntity::animationTick)
                    : createTickerHelper(blockEntityType, setBlockEntity(), AbstractPotBlockEntity::cookingTick);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return setBlockEntity().create(pos, state);
    }

    abstract public BlockEntityType<? extends AbstractPotBlockEntity> setBlockEntity();

    abstract public VoxelShape setShapeWithoutLid();

    abstract public VoxelShape setShapeWithLid();

    abstract public VoxelShape setShapeWithDrive();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(LID);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
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
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
                                  BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        dropContents(state, level, pos, newState, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public NonNullList<ItemStack> getCustomStackList(BlockState state, Level level, BlockPos pos, BlockState newState, Block block) {
        return (level.getBlockEntity(pos) instanceof AbstractPotBlockEntity potBlockEntity)
                ? NonNullList.copyOf(potBlockEntity.getItemStackListInPot(true, true))
                : CSimpleDropContentsBlock.super.getCustomStackList(state, level, pos, newState, block);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> getDrops = super.getDrops(state,builder);
        if (state.getValue(LID).equals(PotState.WITH_LID)){
            for (IPotInteractionStrategy strategy : PotStrategyHandler.getInstance().getMap().get(getDescriptionId())){
                if (!(strategy instanceof LidStrategy lidStrategy)) continue;
                getDrops.add(lidStrategy.lid());
            }
        }
        return getDrops;
    }
}
