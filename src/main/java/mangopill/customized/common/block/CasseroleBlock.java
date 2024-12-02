package mangopill.customized.common.block;

import com.mojang.serialization.MapCodec;
import mangopill.customized.common.block.entity.CasseroleBlockEntity;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.common.registry.ModSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.common.block.state.PotState.WITH_LID;

public class CasseroleBlock extends AbstractPotBlock{
    public static final MapCodec<CasseroleBlock> CODEC = simpleCodec(CasseroleBlock::new);

    protected static final VoxelShape BLOCK_SHAPE_WITHOUT_LID = Shapes.or(
            Block.box(13, 1, 3, 14, 3, 13),
            Block.box(1, 3, 2, 2, 8, 14),
            Block.box(0.5, 8, 2, 1.5, 9, 14),
            Block.box(1, 8, 1, 2, 9, 2),
            Block.box(1, 8, 14, 2, 9, 15),
            Block.box(14, 8, 14, 15, 9, 15),
            Block.box(14, 8, 1, 15, 9, 2),
            Block.box(14.5, 8, 2, 15.5, 9, 14),
            Block.box(14, 3, 2, 15, 8, 14),
            Block.box(2, 8, 0.5, 14, 9, 1.5),
            Block.box(2, 8, 14.5, 14, 9, 15.5),
            Block.box(3, 1, 2, 13, 3, 3),
            Block.box(2, 3, 1, 14, 8, 2),
            Block.box(2, 3, 14, 14, 8, 15),
            Block.box(3, 1, 13, 13, 3, 14),
            Block.box(2, 1, 3, 3, 3, 13),
            Block.box(3, 0, 3, 13, 1, 13),
            Block.box(2, 2, 2, 3, 3, 3),
            Block.box(2, 2, 13, 3, 3, 14),
            Block.box(13, 2, 13, 14, 3, 14),
            Block.box(13, 2, 2, 14, 3, 3)
    );
    protected static final VoxelShape BLOCK_SHAPE_WITH_LID = Shapes.or(
            BLOCK_SHAPE_WITHOUT_LID,
            Block.box(12, 8, 4, 14, 8.5, 12),
            Block.box(2, 8, 2, 14, 8.5, 4),
            Block.box(4, 8.5, 4, 12, 9, 12),
            Block.box(2, 8, 4, 4, 8.5, 12),
            Block.box(2, 8, 12, 14, 8.5, 14),
            Block.box(6.5, 9.5, 6.5, 9.5, 10, 9.5),
            Block.box(7, 9.5, 7, 9, 10.5, 9),
            Block.box(7, 9, 7, 9, 9.5, 9)
    );

    public CasseroleBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    @Override
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CasseroleBlockEntity cookingPotEntity && cookingPotEntity.isHeated() && !state.getValue(LID).equals(PotState.WITHOUT_LID)) {
            double x = (double) pos.getX() + 0.4D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.4D;
            if (random.nextInt(8) == 0) {
                SoundEvent sound = state.getValue(LID).equals(PotState.WITH_LID)
                        ? ModSoundRegistry.BOILING_WATER_WITH_LID.get()
                        : ModSoundRegistry.BOILING_WATER_WITHOUT_LID.get();
                level.playLocalSound(x, y, z, sound, SoundSource.BLOCKS, random.nextFloat() + 0.3F, 0.8F, false);
            }
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (!state.getValue(LID).equals(PotState.WITHOUT_LID)){
            if (level.isClientSide) {
                return createTickerHelper(blockEntityType, PotRecord.CASSEROLE.entityType(), CasseroleBlockEntity::animationTick);
            }
            return createTickerHelper(blockEntityType, PotRecord.CASSEROLE.entityType(), CasseroleBlockEntity::cookingTick);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return PotRecord.CASSEROLE.entityType().create(pos, state);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        if (state.getValue(LID).equals(WITH_LID)){
            List<ItemStack> getDrops = super.getDrops(state,builder);
            getDrops.add(new ItemStack(ModItemRegistry.CASSEROLE_ILD.get()));
            return getDrops;
        }
        return super.getDrops(state,builder);
    }

    @Override
    public @NotNull VoxelShape setShapeWithoutLid() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }

    @Override
    public @NotNull VoxelShape setShapeWithLid() {
        return BLOCK_SHAPE_WITH_LID;
    }

    @Override
    public @NotNull VoxelShape setShapeWithDrive() {
        return BLOCK_SHAPE_WITHOUT_LID;
    }
}
