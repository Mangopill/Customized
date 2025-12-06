package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.block.record.PotRecord;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Customized.MODID)
public class PotCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                PotRecord.CASSEROLE.entityType(),
                (entity, context) -> entity.getInputAndOutputHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                PotRecord.ROASTER.entityType(),
                (entity, context) -> entity.getInputAndOutputHandler()
        );
    }
    @SubscribeEvent
    public static void registerFluidCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                PotRecord.CASSEROLE.entityType(),
                (entity, context) -> entity.getFluidHandler()
        );
    }
}
