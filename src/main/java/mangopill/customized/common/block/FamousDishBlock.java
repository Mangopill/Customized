package mangopill.customized.common.block;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
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

import javax.annotation.Nonnull;

public class FamousDishBlock extends Block {
    public static final IntegerProperty EAT = IntegerProperty.create("eat", 0, 2);
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(3, 1, 4, 4, 3, 12),
            Block.box(3, 1, 12, 13, 3, 13),
            Block.box(3, 3, 2, 13, 6, 3),
            Block.box(2, 3, 3, 3, 6, 13),
            Block.box(13, 3, 3, 14, 6, 13),
            Block.box(3, 3, 13, 13, 6, 14),
            Block.box(3, 1, 3, 13, 3, 4),
            Block.box(12, 1, 4, 13, 3, 12)
    );
    private final FoodProperties foodProperties;

    public FamousDishBlock(Properties properties, FoodProperties foodProperties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(EAT, 0));
        this.foodProperties = foodProperties;
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
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
        return InteractionResult.SUCCESS;
    }

    protected void eat(Level level, Player player, BlockPos pos) {
        player.getFoodData().eat(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
        for (Pair<MobEffectInstance, Float> effect : foodProperties.getEffects()) {
            if (!level.isClientSide && effect != null && level.random.nextFloat() < effect.getSecond()) {
                player.addEffect(effect.getFirst());
            }
        }
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(EAT);
    }

    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nonnull VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(EAT, 0);
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }
}
