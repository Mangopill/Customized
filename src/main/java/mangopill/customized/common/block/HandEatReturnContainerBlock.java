package mangopill.customized.common.block;

import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;

import java.util.function.Supplier;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public class HandEatReturnContainerBlock extends Block {
    public static final IntegerProperty EAT = IntegerProperty.create("eat", 0, 10);
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D),
            Block.box(3.0D, 1.0D, 4.0D, 4.0D, 3.0D, 12.0D),
            Block.box(3.0D, 1.0D, 12.0D, 13.0D, 3.0D, 13.0D),
            Block.box(3.0D, 3.0D, 2.0D, 13.0D, 6.0D, 3.0D),
            Block.box(2.0D, 3.0D, 3.0D, 3.0D, 6.0D, 13.0D),
            Block.box(13.0D, 3.0D, 3.0D, 14.0D, 6.0D, 13.0D),
            Block.box(3.0D, 3.0D, 13.0D, 13.0D, 6.0D, 14.0D),
            Block.box(3.0D, 1.0D, 3.0D, 13.0D, 3.0D, 4.0D),
            Block.box(12.0D, 1.0D, 4.0D, 13.0D, 3.0D, 12.0D)
    );
    private final FoodProperties foodProperties;
    private final Supplier<Item> container;
    private final int maxEat;

    public HandEatReturnContainerBlock(Properties properties, FoodProperties foodProperties, Supplier<Item> container, int maxEat) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(EAT, 0));
        this.foodProperties = foodProperties;
        this.container = container;
        this.maxEat = maxEat;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos, Player player,
                                           InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (foodProperties != null) {
            int eat = state.getValue(EAT);
            if (eat < maxEat) {
                eat(level, player, pos);
                level.setBlockAndUpdate(pos, state.setValue(EAT, eat + 1));
            } else {
                addItemToPlayer(player, container.get().getDefaultInstance());
                level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 0.8F, 1.0F);
                level.removeBlock(pos, false);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    protected void eat(Level level, Player player, BlockPos pos) {
        player.getFoodData().eat(foodProperties);
        for (FoodProperties.PossibleEffect effect : foodProperties.effects()) {
            if (!level.isClientSide && effect != null && level.random.nextFloat() < effect.probability()) {
                player.addEffect(effect.effect());
            }
        }
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(EAT);
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
                .setValue(EAT, 0);
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }

    public Supplier<Item> getContainer() {
        return container;
    }

    public int getMaxEat() {
        return maxEat;
    }
}
