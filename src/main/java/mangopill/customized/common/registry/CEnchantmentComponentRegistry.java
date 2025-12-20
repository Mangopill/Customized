package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CEnchantmentComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENT_TYPE = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Customized.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> SHARED_FEAST =
            ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("shared_feast",
                    () -> DataComponentType.<EnchantmentValueEffect>builder()
                            .persistent(EnchantmentValueEffect.CODEC)
                            .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> DIARRHEA_DELIVERY =
            ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("diarrhea_delivery",
                    () -> DataComponentType.<EnchantmentValueEffect>builder()
                            .persistent(EnchantmentValueEffect.CODEC)
                            .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> AROMA_PERVASION =
            ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("aroma_pervasion",
                    () -> DataComponentType.<EnchantmentValueEffect>builder()
                            .persistent(EnchantmentValueEffect.CODEC)
                            .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> AURA_OF_CULINARY_ARTS =
            ENCHANTMENT_EFFECT_COMPONENT_TYPE.register("aura_of_culinary_arts",
                    () -> DataComponentType.<EnchantmentValueEffect>builder()
                            .persistent(EnchantmentValueEffect.CODEC)
                            .build());
}
