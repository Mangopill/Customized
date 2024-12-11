package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.block.handler.PotFluidHandler;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.registry.ModBlockRegistry;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PotCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                PotRecord.CASSEROLE.entityType(),
                (entity, context) -> {
                    if (context == Direction.UP) {
                        return entity.getInputHandler();
                    }
                    return entity.getOutputHandler();
                }
        );
    }
    @SubscribeEvent
    public static void registerFluidCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, context) -> new PotFluidHandler(level, pos),
                ModBlockRegistry.CASSEROLE.get());
    }
}
