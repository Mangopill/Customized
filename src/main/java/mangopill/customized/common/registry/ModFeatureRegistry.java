package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.world.feature.FlowerAndDirtFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModFeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(Registries.FEATURE, Customized.MODID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> FLOWER_AND_DIRT = FEATURE.register("flower_and_dirt", () -> new FlowerAndDirtFeature(NoneFeatureConfiguration.CODEC));
}
