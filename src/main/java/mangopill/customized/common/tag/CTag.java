package mangopill.customized.common.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import static mangopill.customized.common.util.TagUtil.*;

public final class CTag {
    private CTag() {
    }

    public static final TagKey<Fluid> WATER = basicCFluidTag("water");
    public static final TagKey<Fluid> SOUP = basicCFluidTag("soup");
}
