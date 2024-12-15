package mangopill.customized.common.block;

import mangopill.customized.common.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;

public class FamousDishBlock extends Block {
    public static final IntegerProperty EAT = IntegerProperty.create("eat", 0, 2);
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(2, 0, 2, 14,1,14));
    private final FoodProperties foodProperties;

    public FamousDishBlock(Properties properties, FoodProperties foodProperties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(EAT, 0));
        this.foodProperties = foodProperties;
    }

    @Override
    @NotNull
    public ItemInteractionResult useItemOn(
            @NotNull ItemStack itemStackInHand, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        if (foodProperties != null) {
            int eat = state.getValue(EAT);
            if (eat < 2) {
                eat(level, player, pos);
                level.setBlockAndUpdate(pos, state.setValue(EAT, eat + 1));
            } else {
                if (!player.getInventory().add(ModItemRegistry.FAMOUS_DISH_PLATE.get().getDefaultInstance())) {
                    player.drop(ModItemRegistry.FAMOUS_DISH_PLATE.get().getDefaultInstance(), false);
                }
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
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(EAT);
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
                .setValue(EAT, 0);
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }
}
