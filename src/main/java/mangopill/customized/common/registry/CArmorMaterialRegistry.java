package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.item.CHatItem;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class CArmorMaterialRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL = DeferredRegister.create(Registries.ARMOR_MATERIAL, Customized.MODID);

    public static final Holder<ArmorMaterial> CHEF =
            ARMOR_MATERIAL.register("chef", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(CHatItem.Type.class), map -> {
                        map.put(CHatItem.Type.BOOTS, 1);
                        map.put(CHatItem.Type.LEGGINGS, 2);
                        map.put(CHatItem.Type.CHESTPLATE, 3);
                        map.put(CHatItem.Type.HELMET, 2);
                        map.put(CHatItem.Type.BODY, 3);
                    }),
                    20,
                    Holder.direct(SoundEvents.WOOL_BREAK),
                    () -> Ingredient.of(Tags.Items.STRINGS),
                    List.of(),
                    1.0F,
                    0.0F
            ));
    public static final Holder<ArmorMaterial> NETHERITE_CHEF =
            ARMOR_MATERIAL.register("netherite_chef", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(CHatItem.Type.class), map -> {
                        map.put(CHatItem.Type.BOOTS, 3);
                        map.put(CHatItem.Type.LEGGINGS, 6);
                        map.put(CHatItem.Type.CHESTPLATE, 8);
                        map.put(CHatItem.Type.HELMET, 3);
                        map.put(CHatItem.Type.BODY, 11);
                    }),
                    20,
                    Holder.direct(SoundEvents.WOOL_BREAK),
                    () -> Ingredient.of(Tags.Items.STRINGS),
                    List.of(),
                    4.0F,
                    0.1F
            ));
}
