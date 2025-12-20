package mangopill.customized.common.item;

import net.minecraft.world.item.BucketItem;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.function.Supplier;

public class CBucketItem extends BucketItem {
    public CBucketItem(Supplier<BaseFlowingFluid> content, Properties properties) {
        super(content.get(), properties);
    }
}
