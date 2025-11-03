package mangopill.customized.common.item;

import mangopill.customized.common.block.record.*;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class SoupBowlItem extends AbstractPlateItem {
    public SoupBowlItem(Supplier<Block> block, Properties properties) {
        super(block, properties, PlateSlotRecord.SOUP_BOWL.ingredientInput(), PlateSlotRecord.SOUP_BOWL.ingredientInput(), PlateSlotRecord.SOUP_BOWL.spiceInput(), true);
    }
}
