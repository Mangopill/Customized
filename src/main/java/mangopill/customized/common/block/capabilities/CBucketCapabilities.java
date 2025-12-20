package mangopill.customized.common.block.capabilities;

import mangopill.customized.Customized;
import mangopill.customized.common.item.CBucketItem;
import mangopill.customized.common.registry.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = Customized.MODID)
public class CBucketCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        CItemRegistry.ITEM.getEntries().stream().map(DeferredHolder::get).filter(CBucketItem.class::isInstance)
                .forEach(item -> event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), item));
    }
}
