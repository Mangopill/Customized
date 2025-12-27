package mangopill.customized.common.block;

import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.InteractUtil.*;
import static mangopill.customized.common.util.component.CItemMatchMode.*;

public class SaltPanBlock extends Block {
    public static final BooleanProperty WITH_WATER = BooleanProperty.create("with_water");
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D),
            Block.box(0.0D, 1.0D, 0.0D, 1.0D, 2.0D, 16.0D),
            Block.box(1.0D, 1.0D, 0.0D, 15.0D, 2.0D, 1.0D),
            Block.box(1.0D, 1.0D, 15.0D, 15.0D, 2.0D, 16.0D),
            Block.box(15.0D, 1.0D, 0.0D, 16.0D, 2.0D, 16.0D)
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
        if (simpleTest(itemStackInHand, shrinkItem, SAME_ITEM)){
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
