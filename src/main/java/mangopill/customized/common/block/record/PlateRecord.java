package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record PlateRecord(int ingredientInput,
                          int seasoningInput,
                          BlockEntityType<? extends AbstractPlateBlockEntity> type) {
    public static final PlateRecord SOUP_BOWL = new PlateRecord(6, 6,
            ModBlockEntityTypeRegistry.SOUP_BOWL.get());
}
