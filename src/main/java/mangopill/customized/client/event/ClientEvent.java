package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.particle.DirtParticle;
import mangopill.customized.client.renderer.ModBrushableBlockRederer;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), ModBrushableBlockRederer::new);
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypeRegistry.DIRT.get(), DirtParticle.DirtParticleProvider::new);
    }
}
