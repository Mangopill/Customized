package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.*;

@EventBusSubscriber(modid = Customized.MODID)
public class BrewingBarrelCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CBlockEntityTypeRegistry.BREWING_BARREL.get(),
                (entity, context) -> entity.getInputAndOutputHandler()
        );
    }
}
