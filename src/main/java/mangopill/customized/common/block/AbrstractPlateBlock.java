package mangopill.customized.common.block;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

public abstract class AbrstractPlateBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    protected AbrstractPlateBlock(Properties properties) {
        super(properties);
    }
}
