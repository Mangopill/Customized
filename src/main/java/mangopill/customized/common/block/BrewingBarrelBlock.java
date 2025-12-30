package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.BrewingBarrelBlockEntity;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.minecraft.core.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelBlock extends BaseEntityBlock implements CSimpleInteractableBlock, CSimpleDropContentsBlock {
    public static final MapCodec<BrewingBarrelBlock> CODEC = simpleCodec(BrewingBarrelBlock::new);
    public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, 12);

    public BrewingBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(PROGRESS, 0)
        );
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult result) {
        return simpleInteract(itemStackInHand, state, level, pos, player, hand, SoundEvents.BARREL_CLOSE, SoundEvents.BARREL_OPEN);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        dropContents(state, level, pos, newState, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, CBlockEntityTypeRegistry.BREWING_BARREL.get(), BrewingBarrelBlockEntity::cookingTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(PROGRESS);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(PROGRESS, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return CBlockEntityTypeRegistry.BREWING_BARREL.get().create(blockPos, blockState);
    }
}
