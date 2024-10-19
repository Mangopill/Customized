package mangopill.customized;

import mangopill.customized.common.registry.ModBlcokRegistry;
import mangopill.customized.common.registry.ModBlockEntityRegistry;
import mangopill.customized.common.registry.ModCreativeModeTabRegistry;
import mangopill.customized.common.registry.ModItemRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Customized.MODID)
public class Customized {
    public static final String MODID = "customized";

    public Customized(IEventBus modBus) {
        ModBlcokRegistry.BLOCKS.register(modBus);
        ModItemRegistry.ITEMS.register(modBus);
        ModBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        ModCreativeModeTabRegistry.CREATIVE_MODE_TAB.register(modBus);
    }
}