package mangopill.customized.common.item;

import mangopill.customized.common.block.record.PlateSlotRecord;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BakingPanItem extends AbstractPlateItem {
    public BakingPanItem(Supplier<Block> block, Properties properties) {
        super(block, properties, PlateSlotRecord.BAKING_PAN.ingredientInput(), PlateSlotRecord.BAKING_PAN.ingredientInput(), PlateSlotRecord.BAKING_PAN.spiceInput(), true);
    }
}
