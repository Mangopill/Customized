package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.event.renderer.*;
import mangopill.customized.client.particle.*;
import mangopill.customized.common.item.*;
import mangopill.customized.common.registry.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import static mangopill.customized.client.event.tinting.Tinting.*;

@EventBusSubscriber(modid = Customized.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void registerOverride(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItemRegistry.SOUP_BOWL.get(),
                ResourceLocation.fromNamespaceAndPath(Customized.MODID, "drive"),
                (stack, level, player, seed) -> stack.getItem() instanceof SoupBowlItem ?
                        ((SoupBowlItem) stack.getItem()).hasInput(stack) ? 1.0F : 0.0F : 0.0F));
    }
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(Customized.MODID, "item/soup_bowl_with_drive_renderer")
        ));
    }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new SoupBowlItemRenderer.SoupBowlItemExtensions(), ModItemRegistry.SOUP_BOWL.get());
    }
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), ModBrushableBlockRederer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.CASSEROLE.get(), CasseroleBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SOUP_BOWL.get(), SoupBowlBlockRenderer::new);
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypeRegistry.DIRT.get(), DirtParticle.DirtParticleProvider::new);
        event.registerSpriteSet(ModParticleTypeRegistry.STEAM.get(), SteamParticle.SteamParticleProvider::new);
        event.registerSpriteSet(ModParticleTypeRegistry.AROMA.get(), AromaParticle.AromaParticleProvider::new);
    }
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level == null || pos == null ? -1 : getPotWaterColor(level, state, pos), ModBlockRegistry.CASSEROLE.get());
        event.register((state, level, pos, tintIndex) -> level == null || pos == null ? -1 : getPlateBlockWaterColor(level, state, pos), ModBlockRegistry.SOUP_BOWL.get());
    }
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> getPlateItemWaterColor(stack), ModItemRegistry.SOUP_BOWL.get());
    }
}
