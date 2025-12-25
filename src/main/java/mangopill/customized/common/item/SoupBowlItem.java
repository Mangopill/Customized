package mangopill.customized.common.item;

import mangopill.customized.common.block.record.PlateRecord;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class SoupBowlItem extends AbstractPlateItem {
    public SoupBowlItem(Supplier<Block> block, Properties properties) {
        super(block, properties, PlateRecord.SOUP_BOWL);
    }
}
