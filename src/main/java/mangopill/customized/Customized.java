package mangopill.customized;

import mangopill.customized.common.CustomizedConfig;
import mangopill.customized.common.registry.*;
import mangopill.customized.integration.curios.CuriosRegistry;
import mangopill.customized.integration.curios.common.CuriosItemRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
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
        CItemRegistry.ITEM.register(modBus);
        CBlockRegistry.BLOCK.register(modBus);
        CSoundRegistry.SOUND_EVENT.register(modBus);
        CBlockEntityTypeRegistry.BLOCK_ENTITY_TYPE.register(modBus);
        CCreativeModeTabRegistry.CREATIVE_MODE_TAB.register(modBus);
        CFeatureRegistry.FEATURE.register(modBus);
        CParticleTypeRegistry.PARTICLE_TYPE.register(modBus);
        CRecipeRegistry.RECIPE_TYPE.register(modBus);
        CRecipeSerializerRegistry.RECIPE_SERIALIZER.register(modBus);
        CEffectRegistry.MOB_EFFECT.register(modBus);
        CDataComponentRegistry.DATA_COMPONENT.register(modBus);
        CAdvancementRegistry.TRIGGER.register(modBus);
        CArmorMaterialRegistry.ARMOR_MATERIAL.register(modBus);
        CEnchantmentComponentRegistry.ENCHANTMENT_EFFECT_COMPONENT_TYPE.register(modBus);
        if (ModList.get().isLoaded("curios")) {
            modBus.addListener(CuriosRegistry::commonSetup);
            CuriosItemRegistry.ITEM.register(modBus);
            if (FMLEnvironment.dist == Dist.CLIENT) {
                modBus.addListener(CuriosRegistry::clientSetup);
            }
        }
    }
}