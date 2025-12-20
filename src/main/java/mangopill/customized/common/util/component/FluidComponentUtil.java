package mangopill.customized.common.util.component;

import mangopill.customized.common.registry.CDataComponentRegistry;
import net.neoforged.neoforge.common.MutableDataComponentHolder;

public final class FluidComponentUtil {
    public static final int NO_TINT = 0XFFFFFFFF;

    private FluidComponentUtil() {
    }

    public static int getColor(MutableDataComponentHolder holder) {
        return holder.getOrDefault(CDataComponentRegistry.ARGB_COLOR, NO_TINT);
    }

    public static void setColor(MutableDataComponentHolder holder, int color) {
        holder.set(CDataComponentRegistry.ARGB_COLOR, color);
    }
}
