package mangopill.customized.common.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.*;

public class ModHatItem extends ArmorItem {
    private final double translateY;
    private final float scale;

    public ModHatItem(Holder<ArmorMaterial> material, Type type, Properties properties, double translateY, float scale) {
        super(material, type, properties);
        this.translateY = translateY;
        this.scale = scale;
    }

    public double getTranslateY() {
        return translateY;
    }

    public float getScale() {
        return scale;
    }
}
