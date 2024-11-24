package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.event.renderer.*;
import mangopill.customized.client.particle.*;
import mangopill.customized.common.registry.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import static mangopill.customized.client.event.tinting.PotTinting.*;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), ModBrushableBlockRederer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.CASSEROLE.get(), CasseroleBlockRenderer::new);
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypeRegistry.DIRT.get(), DirtParticle.DirtParticleProvider::new);
        event.registerSpriteSet(ModParticleTypeRegistry.STEAM.get(), SteamParticle.SteamParticleProvider::new);
    }
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level == null || pos == null ? -1 : getWaterColor(level, state, pos), ModBlcokRegistry.CASSEROLE.get());
    }
}
