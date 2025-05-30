package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.event.renderer.*;
import mangopill.customized.client.particle.*;
import mangopill.customized.common.item.*;
import mangopill.customized.common.registry.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static mangopill.customized.client.event.tinting.Tinting.*;

@Mod.EventBusSubscriber(modid = Customized.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        event.register(new ModelResourceLocation(Customized.MODID, "soup_bowl_with_drive_renderer", "inventory"));
    }
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), ModBrushableBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.CASSEROLE.get(), CasseroleBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.SOUP_BOWL.get(), SoupBowlBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypeRegistry.BREWING_BARREL.get(), BrewingBarrelBlockRenderer::new);
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
