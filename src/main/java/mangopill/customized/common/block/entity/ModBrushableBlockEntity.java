package mangopill.customized.common.block.entity;

import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ModBrushableBlockEntity extends BrushableBlockEntity {

    public ModBrushableBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }
    @Override
    public @NotNull BlockEntityType<?> getType()
    {
        return ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get();
    }

}
