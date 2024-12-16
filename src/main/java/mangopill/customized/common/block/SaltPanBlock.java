package mangopill.customized.common.block;

import mangopill.customized.common.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;

public class SaltPanBlock extends Block {
    public static final BooleanProperty WITH_WATER = BooleanProperty.create("with_water");
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 1, 0, 1, 2, 16),
            Block.box(1, 1, 0, 15, 2, 1),
            Block.box(1, 1, 15, 15, 2, 16),
            Block.box(15, 1, 0, 16, 2, 16)
    );

    public SaltPanBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(WITH_WATER, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    @NotNull
    public ItemInteractionResult useItemOn(
            @NotNull ItemStack itemStackInHand, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (level.isClientSide){
            return ItemInteractionResult.SUCCESS;
        }
        if (state.getValue(WITH_WATER).equals(false)){
            shrinkAndGive(itemStackInHand, state, level, pos, player, Items.POTION, Items.GLASS_BOTTLE, SoundEvents.BOTTLE_EMPTY,true);
        } else {
            shrinkAndGive(itemStackInHand, state, level, pos, player, Items.GLASS_BOTTLE, Items.POTION, SoundEvents.BOTTLE_FILL,false);
        }
        return ItemInteractionResult.SUCCESS;
    }

    protected void shrinkAndGive(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                                 Item shrinkItem, Item giveItem, SoundEvent sound, boolean with_water) {
        if (itemStackInHand.is(shrinkItem)){
            if (!player.isCreative()) {
                itemStackInHand.shrink(1);
                if (!player.getInventory().add(giveItem.getDefaultInstance())) {
                    player.drop(giveItem.getDefaultInstance(), false);
                }
            }
            level.setBlockAndUpdate(pos, state.setValue(WITH_WATER, with_water));
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.canSeeSky(pos) || !level.isDay() || state.getValue(WITH_WATER).equals(false)) {
            level.scheduleTick(pos, this, Math.max(10000, RandomSource.create().nextInt(12000)));
            return;
        }
        level.setBlockAndUpdate(pos, state.setValue(WITH_WATER, false));
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY() + 0.5D, pos.getZ(),
                ModItemRegistry.SALT.get().getDefaultInstance());
        itemEntity.setDeltaMovement(0.0D, 0.01D, 0.0D);
        level.addFreshEntity(itemEntity);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, Math.max(10000, RandomSource.create().nextInt(12000)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(WITH_WATER);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(WITH_WATER, false);
    }
}
