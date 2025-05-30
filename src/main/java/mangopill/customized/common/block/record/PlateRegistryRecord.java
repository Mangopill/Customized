package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record PlateRegistryRecord(BlockEntityType<? extends AbstractPlateBlockEntity> type) {
    public static final PlateRegistryRecord SOUP_BOWL = new PlateRegistryRecord(ModBlockEntityTypeRegistry.SOUP_BOWL.get());
}
