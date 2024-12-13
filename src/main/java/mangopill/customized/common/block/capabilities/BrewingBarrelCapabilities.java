package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BrewingBarrelCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypeRegistry.BREWING_BARREL.get(),
                (entity, context) -> {
                    if (context == Direction.UP) {
                        return entity.getInputHandler();
                    }
                    return entity.getOutputHandler();
                }
        );
    }
}
