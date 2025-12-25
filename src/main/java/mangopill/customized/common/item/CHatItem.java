package mangopill.customized.common.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

public class CHatItem extends ArmorItem {
    private final ResourceLocation texture;
    private final double translateY;
    private final float scale;

    public CHatItem(Holder<ArmorMaterial> material, Type type, Properties properties, ResourceLocation texture, double translateY, float scale) {
        super(material, type, properties);
        this.texture = texture;
        this.translateY = translateY;
        this.scale = scale;
    }

    @Nullable
    @Override
    public  ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return slot == EquipmentSlot.HEAD ? texture : null;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public double getTranslateY() {
        return translateY;
    }

    public float getScale() {
        return scale;
    }
}
