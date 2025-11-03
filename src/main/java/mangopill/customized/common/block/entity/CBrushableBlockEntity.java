package mangopill.customized.common.block.entity;

import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CBrushableBlockEntity extends BrushableBlockEntity {
    public CBrushableBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return CBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get();
    }
}
