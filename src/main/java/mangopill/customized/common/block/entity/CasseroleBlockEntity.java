package mangopill.customized.common.block.entity;

import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CasseroleBlockEntity extends AbstractPotBlockEntity{
    public CasseroleBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypeRegistry.CASSEROLE.get(), pos, blockState, 6, 6, "casserole");
    }
    public static BlockEntityType<? extends AbstractPotBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypeRegistry.CASSEROLE.get();
    }
}
