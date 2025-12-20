package mangopill.customized.common.block;

import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

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
    public ItemInteractionResult useItemOn(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
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
            consumeItemAndGiveToPlayer(itemStackInHand, player, giveItem.getDefaultInstance());
            level.setBlockAndUpdate(pos, state.setValue(WITH_WATER, with_water));
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.canSeeSky(pos) || !level.isDay() || state.getValue(WITH_WATER).equals(false)) {
            level.scheduleTick(pos, this, Math.max(10000, RandomSource.create().nextInt(13000)));
            return;
        }
        level.setBlockAndUpdate(pos, state.setValue(WITH_WATER, false));
        spawnItemEntity(level, CItemRegistry.SALT.get().getDefaultInstance(), null, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, Math.max(10000, RandomSource.create().nextInt(13000)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(WITH_WATER);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(WITH_WATER, false);
    }
}
