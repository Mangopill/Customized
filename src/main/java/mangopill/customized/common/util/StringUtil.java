package mangopill.customized.common.util;

import mangopill.customized.Customized;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;

public final class StringUtil {
    private StringUtil() {
    }

    public static ResourceLocation getCLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Customized.MODID, path);
    }

    public static ResourceLocation getCPngLoc(String path) {
        return getCLoc(path + ".png");
    }

    public static MutableComponent getComponent(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent getComponent(String key, Object... args) {
        return Component.translatable(key, args);
    }

    public static TextColor getColorFromString(String color) {
        return TextColor.fromRgb(getColorWithAlphaFromString(color));
    }

    public static int getColorWithAlphaFromString(String color) {
        return Long.decode(color).intValue();
    }
}
