package mangopill.customized.common.util.category;

import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringRepresentable;

public enum NutrientCategory implements StringRepresentable {
    WATER("WATER", 0x446FE9),
    PROTEIN("PROTEIN", 0xE39191),
    LIPID("LIPID", 0xF9B2BA),
    CARBOHYDRATE("CARBOHYDRATE", 0xEAEAEA),
    VITAMIN("VITAMIN", 0xFFA73F),
    MINERAL("MINERAL", 0x574D39),
    DIETARY_FIBER("DIETARY_FIBER", 0x1F9A1C),

    COLD("COLD", 0x06DBD6),
    WARM("WARM", 0xFF6B36),

    ECOLOGY("ECOLOGY", 0x76B64C),
    DREAD("DREAD", 0x01A7AC),
    NOTHINGNESS("NOTHINGNESS", 0x003152),

    SOUR("SOUR", 0xFFF700),
    SWEET("SWEET", 0xFFB6C1),
    BITTER("BITTER", 0x6B4F3B),
    SPICY("SPICY", 0xFF4500),
    SALTY("SALTY", 0x1E90FF),
    NUMBING("NUMBING", 0xB86B7D);

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
    public String getSerializedName() {
        return name;
    }
}
