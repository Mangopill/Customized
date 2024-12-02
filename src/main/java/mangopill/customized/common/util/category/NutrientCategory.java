package mangopill.customized.common.util.category;

import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum NutrientCategory implements StringRepresentable {
    WATER("water", 0x5A8FE9),
    PROTEIN("protein", 0xE39191),
    LIPID("lipid", 0xF9B2BA),
    CARBOHYDRATE("carbohydrate", 0xEAEAEA),
    VITAMIN("vitamin", 0xFFA73F),
    MINERAL("mineral", 0x574D39),
    DIETARY_FIBER("dietary_fiber", 0x1F9A1C),

    COLD("cold", 0x06DBD6),
    WARM("warm", 0xFF6B36),

    ECOLOGY("ecology", 0x76B64C),
    DREAD("dread", 0x01A7AC),
    NOTHINGNESS("nothingness", 0x003152),

    SOUR("sour", 0xFFF700),
    SWEET("sweet", 0xFFB6C1),
    BITTER("bitter", 0x6B4F3B),
    SPICY("spicy", 0xFF4500),
    SALTY("salty", 0x1E90FF),
    NUMBING("numbing", 0xB86B7D);

    private final String name;
    private final TextColor color;

    NutrientCategory(String name, int color) {
        this.name = name;
        this.color = TextColor.fromRgb(color);
    }

    public TextColor getColor() {
        return color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
