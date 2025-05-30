package mangopill.customized.common.item;

import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.block.record.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SoupBowlItem extends AbstractPlateItem {
    public SoupBowlItem(Block block, Properties properties) {
        super(block, properties, PlateSlotRecord.SOUP_BOWL.ingredientInput(), PlateSlotRecord.SOUP_BOWL.ingredientInput(), true);
    }

    @Override
    public AbstractPotBlockEntity getPotEntity(Level level, BlockPos pos) {
        return PotRecord.CASSEROLE.entityType().getBlockEntity(level, pos);
    }
}
