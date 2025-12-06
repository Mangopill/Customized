package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record PlateRegistryRecord(BlockEntityType<? extends AbstractPlateBlockEntity> type) {
    public static final PlateRegistryRecord SOUP_BOWL = new PlateRegistryRecord(CBlockEntityTypeRegistry.SOUP_BOWL.get());
    public static final PlateRegistryRecord BAKING_PAN = new PlateRegistryRecord(CBlockEntityTypeRegistry.BAKING_PAN.get());
}
