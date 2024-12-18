package mangopill.customized;

import mangopill.customized.common.CustomizedConfig;
import mangopill.customized.common.registry.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Customized.MODID)
public class Customized {
    public static final String MODID = "customized";

    public Customized(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CustomizedConfig.COMMON_CONFIG);
        container.registerConfig(ModConfig.Type.CLIENT, CustomizedConfig.CLIENT_CONFIG);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
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
        ModDataComponentRegistry.DATA_COMPONENT.register(modBus);
        ModAdvancementRegistry.TRIGGER.register(modBus);
    }
}