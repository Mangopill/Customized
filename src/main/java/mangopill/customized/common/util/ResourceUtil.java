package mangopill.customized.common.util;

import mangopill.customized.Customized;
import net.minecraft.resources.ResourceLocation;

public final class ResourceUtil {
    private ResourceUtil() {
    }
    public static ResourceLocation getCLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Customized.MODID, path);
    }
}
