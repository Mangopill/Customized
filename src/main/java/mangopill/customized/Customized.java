package mangopill.customized;

import mangopill.customized.common.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Customized.MODID)
public class Customized {
    public static final String MODID = "customized";

    public Customized(IEventBus modBus) {
        ModBlcokRegistry.BLOCKS.register(modBus);
        ModItemRegistry.ITEMS.register(modBus);
        ModBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        ModCreativeModeTabRegistry.CREATIVE_MODE_TAB.register(modBus);
        ModFeatureRegistry.FEATURE.register(modBus);
        ModParticleTypeRegistry.PARTICLE_TYPE.register(modBus);
    }
}