package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public record PlateRecord <T extends AbstractPlateBlockEntity> (Supplier<BlockEntityType<T>> type, int ingredientInput, int seasoningInput, int spiceInput, boolean canChangeState) {
    public static final PlateRecord<SoupBowlBlockEntity> SOUP_BOWL = new PlateRecord<>(CBlockEntityTypeRegistry.SOUP_BOWL, 6, 6, 1, true);
    public static final PlateRecord<BakingPanBlockEntity> BAKING_PAN = new PlateRecord<>(CBlockEntityTypeRegistry.BAKING_PAN, 5, 5, 1, true);
    public static final PlateRecord<DishBlockEntity> DISH = new PlateRecord<>(CBlockEntityTypeRegistry.DISH, 5, 8, 2, true);
    public static final PlateRecord<PlatterBlockEntity> PLATTER = new PlateRecord<>(CBlockEntityTypeRegistry.PLATTER, 5, 4, 1, true);
}
