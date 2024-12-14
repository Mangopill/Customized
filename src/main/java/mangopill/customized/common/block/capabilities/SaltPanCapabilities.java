package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.block.handler.SaltPanFluidHandler;
import mangopill.customized.common.registry.ModBlockRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SaltPanCapabilities {
    @SubscribeEvent
    public static void registerFluidCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, context) -> new SaltPanFluidHandler(level, pos),
                ModBlockRegistry.SALT_PAN.get());
    }
}
