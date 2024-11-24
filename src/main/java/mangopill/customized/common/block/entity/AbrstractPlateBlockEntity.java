package mangopill.customized.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class AbrstractPlateBlockEntity extends BlockEntity {
    private final ItemStackHandler itemStackHandler;

    public AbrstractPlateBlockEntity(BlockEntityType<? extends AbrstractPlateBlockEntity> type, BlockPos pos, BlockState blockState, ItemStackHandler itemStackHandler) {
        super(type, pos, blockState);
        this.itemStackHandler = itemStackHandler;
    }
}
