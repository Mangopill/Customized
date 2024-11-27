package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<PotState> LID = EnumProperty.create("lid", PotState.class);

    protected AbstractPotBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(LID, PotState.WITHOUT_LID)
        );
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(
           @NotNull ItemStack itemStackInHand, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
           @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (!level.isClientSide){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity) {
                if(itemStackInHand.isEmpty()){
                    if (state.getValue(LID).equals(PotState.WITH_LID)){
                        if (canInputDrive()){
                            level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITH_DRIVE));
                            player.getInventory().add(setLid());
                            level.playSound(null, pos, SoundEvents.DECORATED_POT_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
                            return ItemInteractionResult.SUCCESS;
                        } else {
                            level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITHOUT_LID));
                            player.getInventory().add(setLid());
                            level.playSound(null, pos, SoundEvents.DECORATED_POT_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
                            return ItemInteractionResult.SUCCESS;
                        }
                    } else {
                        if (player.isShiftKeyDown()){
                            potBlockEntity.takeOutItem(level, state, pos);
                            level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 0.8F, 1.0F);
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                } else {
                    if (!state.getValue(LID).equals(PotState.WITH_LID)){
                        if (canStirFry() && itemStackInHand.is(ModTag.SPATULA)){
                            potBlockEntity.stirFryAccelerate(itemStackInHand, player, hand);
                            level.playSound(null, pos, SoundEvents.METAL_HIT, SoundSource.BLOCKS, 0.8F, 1.0F);
                            return ItemInteractionResult.SUCCESS;
                        }
                        if (itemStackInHand.is(ModItemRegistry.FAMOUS_DISH_PLATE.get())) {
                            potBlockEntity.getOutputInPot(itemStackInHand, player);
                            level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 0.8F, 1.0F);
                            return ItemInteractionResult.SUCCESS;
                        }
                        if(state.getValue(LID).equals(PotState.WITHOUT_LID)){
                            if (canInputDrive()){
                                if (itemStackInHand.is(Items.WATER_BUCKET)){
                                    level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITH_DRIVE));
                                    itemStackInHand.shrink(1);
                                    if (!player.getInventory().add(Items.BUCKET.getDefaultInstance())) {
                                        player.drop(Items.BUCKET.getDefaultInstance(), false);
                                    }
                                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
                                    return ItemInteractionResult.SUCCESS;
                                }
                            } else {
                                if (canBeCovered() && itemStackInHand.is(setLid().getItem())){
                                    itemStackInHand.shrink(1);
                                    level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITH_LID));
                                    level.playSound(null, pos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
                                    return ItemInteractionResult.SUCCESS;
                                }
                            }
                        } else {
                            if (canBeCovered() && canInputDrive() && itemStackInHand.is(setLid().getItem())){
                                itemStackInHand.shrink(1);
                                level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITH_LID));
                                level.playSound(null, pos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
                                return ItemInteractionResult.SUCCESS;
                            }
                            if (itemStackInHand.is(Items.BUCKET)) {
                                level.setBlockAndUpdate(pos, state.setValue(LID, PotState.WITHOUT_LID));
                                itemStackInHand.shrink(1);
                                if (!player.getInventory().add(Items.WATER_BUCKET.getDefaultInstance())) {
                                    player.drop(Items.WATER_BUCKET.getDefaultInstance(), false);
                                }
                                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
                                return ItemInteractionResult.SUCCESS;
                            }
                        }
                        if (itemStackInHand.getItem() instanceof AbstractPlateItem){
                            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                        }
                        potBlockEntity.insertItem(itemStackInHand);
                        level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 0.8F, 1.0F);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    abstract public boolean canStirFry();

    abstract public boolean canBeCovered();

    abstract public boolean canInputDrive();

    abstract public VoxelShape setShapeWithoutLid();

    abstract public VoxelShape setShapeWithLid();

    abstract public VoxelShape setShapeWithDrive();

    @NotNull
    abstract public ItemStack setLid();

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(LID);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(LID)){
            case WITHOUT_LID -> setShapeWithoutLid();
            case WITH_LID -> setShapeWithLid();
            case WITH_DRIVE -> setShapeWithDrive();
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
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
                .setValue(LID, PotState.WITHOUT_LID);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        if (state.getValue(LID).equals(PotState.WITH_LID)){
           List<ItemStack> getDrops = super.getDrops(state,builder);
            getDrops.add(setLid());
           return getDrops;
        }
        return super.getDrops(state,builder);
    }
}
