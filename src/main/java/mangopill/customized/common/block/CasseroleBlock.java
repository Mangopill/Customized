package mangopill.customized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
public class CasseroleBlock extends AbstractPotBlock{
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
        super(properties, BLOCK_SHAPE_WITHOUT_LID, BLOCK_SHAPE_WITH_LID, BLOCK_SHAPE_WITHOUT_LID);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CasseroleBlockEntity cookingPotEntity && cookingPotEntity.isHeated()) {
            SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty()
                    ? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get()
                    : ModSounds.BLOCK_COOKING_POT_BOIL.get();
            double x = (double) pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.5D;
            if (random.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, boilSound, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.2F + 0.9F, false);
            }
        }*/
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStackInhand, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
/*        if (itemStackInhand.isEmpty() && player.isShiftKeyDown()) {
            level.setBlockAndUpdate(pos, state.setValue(SUPPORT, state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE)
                    ? getTrayState(level, pos) : CookingPotSupport.HANDLE));
            level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
        } else if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CasseroleBlockEntity cookingPotEntity) {
                ItemStack servingStack = cookingPotEntity.useHeldItemOnMeal(heldStack);
                if (servingStack != ItemStack.EMPTY) {
                    if (!player.getInventory().add(servingStack)) {
                        player.drop(servingStack, false);
                    }
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    player.openMenu(cookingPotEntity, pos);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.SUCCESS;*/
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
        /*return ModBlockEntityTypeRegistry.CASSEROLE.get().create(pos, state);*/
    }
}
