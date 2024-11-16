package mangopill.customized.client.event.capabilities;

import mangopill.customized.Customized;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import mangopill.customized.common.block.entity.CasseroleBlockEntity;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PotCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CasseroleBlockEntity.getBlockEntityType(),
                (entity, context) -> {
                    if (context == Direction.UP) {
                        return entity.getInputHandler();
                    }
                    return entity.getOutputHandler();
                }
        );
    }
}
