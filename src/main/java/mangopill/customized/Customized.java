package mangopill.customized;

import mangopill.customized.common.CustomizedConfig;
import mangopill.customized.common.registry.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Customized.MODID)
public class Customized {
    public static final String MODID = "customized";

    public Customized() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModAdvancementRegistry.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CustomizedConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CustomizedConfig.CLIENT_CONFIG);
        ModBlockRegistry.BLOCK.register(modBus);
        ModItemRegistry.ITEM.register(modBus);
        ModSoundRegistry.SOUND_EVENT.register(modBus);
        ModBlockEntityTypeRegistry.BLOCK_ENTITY_TYPE.register(modBus);
        ModCreativeModeTabRegistry.CREATIVE_MODE_TAB.register(modBus);
        ModFeatureRegistry.FEATURE.register(modBus);
        ModParticleTypeRegistry.PARTICLE_TYPE.register(modBus);
        ModRecipeRegistry.RECIPE_TYPE.register(modBus);
        ModRecipeSerializerRegistry.RECIPE_SERIALIZER.register(modBus);
        ModEffectRegistry.MOB_EFFECT.register(modBus);
    }
}