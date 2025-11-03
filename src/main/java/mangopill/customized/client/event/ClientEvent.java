package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.event.renderer.block.*;
import mangopill.customized.client.event.renderer.gui.PotOverlay;
import mangopill.customized.client.event.renderer.item.CrateItemRenderer;
import mangopill.customized.client.event.renderer.item.SoupBowlItemRenderer;
import mangopill.customized.client.event.renderer.player.AuraOfCulinaryArtsEnchantmentEffectRenderer;
import mangopill.customized.client.event.renderer.player.CHatLayerRenderer;
import mangopill.customized.client.particle.*;
import mangopill.customized.common.item.*;
import mangopill.customized.common.registry.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static mangopill.customized.client.util.TintingUtil.*;
import static mangopill.customized.common.util.ResourceUtil.getCLoc;

@EventBusSubscriber(modid = Customized.MODID, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void registerOverride(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(CItemRegistry.SOUP_BOWL.get(), getCLoc("drive"),
                (stack, level, player, seed) -> stack.getItem() instanceof SoupBowlItem ?
                        ((SoupBowlItem) stack.getItem()).hasInput(stack) ? 1.0F : 0.0F : 0.0F));
    }
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(getCLoc("item/soup_bowl_with_drive_renderer")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/crate_renderer")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/armor/chef_hat")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/armor/netherite_chef_hat")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/curio/culinary_masters_hat")));
    }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new SoupBowlItemRenderer.SoupBowlItemExtensions(), CItemRegistry.SOUP_BOWL.get());
        event.registerItem(new CrateItemRenderer.CrateItemExtensions(), CItemRegistry.CRATE.get());
    }
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), CBrushableBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.CASSEROLE.get(), CasseroleBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.SOUP_BOWL.get(), SoupBowlBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.BREWING_BARREL.get(), BrewingBarrelBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.CRATE.get(), CrateBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.CUTTING_BOARD.get(), CuttingBoardBlockRenderer::new);
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CParticleTypeRegistry.DIRT.get(), DirtParticle.DirtParticleProvider::new);
        event.registerSpriteSet(CParticleTypeRegistry.STEAM.get(), SteamParticle.SteamParticleProvider::new);
        event.registerSpriteSet(CParticleTypeRegistry.AROMA.get(), AromaParticle.AromaParticleProvider::new);
    }
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level == null || pos == null ? -1 : getPotWaterColor(level, state, pos), CBlockRegistry.CASSEROLE.get());
        event.register((state, level, pos, tintIndex) -> level == null || pos == null ? -1 : getPlateBlockWaterColor(level, state, pos), CBlockRegistry.SOUP_BOWL.get());
    }
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> getPlateItemWaterColor(stack), CItemRegistry.SOUP_BOWL.get());
    }
    @SubscribeEvent
    public static void onOverlayRegister(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, getCLoc("pot"), new PotOverlay());
    }
    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skinName : event.getSkins()) {
            PlayerRenderer playerRenderer = event.getSkin(skinName);
            if (playerRenderer == null) {
                continue;
            }
            playerRenderer.addLayer(new CHatLayerRenderer<>(playerRenderer));
            playerRenderer.addLayer(new AuraOfCulinaryArtsEnchantmentEffectRenderer<>(playerRenderer));
        }
        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            EntityRenderer<?> renderer = event.getRenderer(entityType);
            if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
                if (livingRenderer.getModel() instanceof HumanoidModel<?>) {
                    livingRenderer.addLayer(new CHatLayerRenderer(livingRenderer));
                }
            }
        }
    }
}
