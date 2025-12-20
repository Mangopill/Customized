package mangopill.customized.client.event;

import mangopill.customized.Customized;
import mangopill.customized.client.event.block.*;
import mangopill.customized.client.event.entity.KnifeEntityRenderer;
import mangopill.customized.client.event.gui.PotOverlay;
import mangopill.customized.client.event.item.*;
import mangopill.customized.client.event.player.AuraOfCulinaryArtsEnchantmentEffectRenderer;
import mangopill.customized.client.event.player.CHatLayerRenderer;
import mangopill.customized.client.particle.*;
import mangopill.customized.common.fluid.type.*;
import mangopill.customized.common.registry.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.client.util.TintingUtil.*;
import static mangopill.customized.client.util.TooltipUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

@EventBusSubscriber(modid = Customized.MODID, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void registerOverride(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerPlateItemProperty(CItemRegistry.SOUP_BOWL.get());
            registerPlateItemProperty(CItemRegistry.BAKING_PAN.get());
        });
        event.enqueueWork(()->{
            ItemBlockRenderTypes.setRenderLayer(CFluidRegistry.SOUP.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(CFluidRegistry.FLOWING_SOUP.get(), RenderType.translucent());
        });
    }
    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        CFluidTypeRegistry.FLUID_TYPE.getEntries().stream().map(DeferredHolder::get)
                .filter(CFluidType.class::isInstance)
                .map(CFluidType.class::cast)
                .forEach(fluidType -> event.registerFluidType(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return fluidType.getStillTexture();
                    }
                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return fluidType.getFlowingTexture();
                    }
                    @Override
                    public int getTintColor() {
                        return (fluidType instanceof TintedFluidType tintedFluidType) ? tintedFluidType.getTintColor() : IClientFluidTypeExtensions.super.getTintColor();
                    }
                    @Override
                    public int getTintColor(FluidStack stack) {
                        return (fluidType instanceof TintedFluidType tintedFluidType) ? tintedFluidType.getTintColor(stack) : IClientFluidTypeExtensions.super.getTintColor(stack);
                    }
                    @Override
                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                        return (fluidType instanceof TintedFluidType tintedFluidType) ? tintedFluidType.getTintColor(state, getter, pos) : IClientFluidTypeExtensions.super.getTintColor(state, getter, pos);
                    }
                }, fluidType));
    }
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(getCLoc("item/soup_bowl_with_drive_renderer")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/baking_pan_with_drive_renderer")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/crate_renderer")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/armor/chef_hat")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/armor/netherite_chef_hat")));
        event.register(ModelResourceLocation.standalone(getCLoc("item/curio/culinary_masters_hat")));
    }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new SoupBowlItemRenderer.SoupBowlItemExtensions(), CItemRegistry.SOUP_BOWL.get());
        event.registerItem(new BakingPanItemRenderer.BakingPanItemExtensions(), CItemRegistry.BAKING_PAN.get());
        event.registerItem(new CrateItemRenderer.CrateItemExtensions(), CItemRegistry.CRATE.get());
    }
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CEntityTypeRegistry.KNIFE.get(), KnifeEntityRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get(), CBrushableBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.CASSEROLE.get(), CasseroleBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.STEAMER.get(), SteamerBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.ROASTER.get(), RoasterBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.SOUP_BOWL.get(), SoupBowlBlockRenderer::new);
        event.registerBlockEntityRenderer(CBlockEntityTypeRegistry.BAKING_PAN.get(), BakingPanBlockRenderer::new);
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
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return;
        propertyValueTooltip(event.getToolTip(), event.getItemStack(), event.getEntity().level());
    }
    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skinName : event.getSkins()) {
            PlayerRenderer playerRenderer = event.getSkin(skinName);
            if (playerRenderer == null) continue;
            playerRenderer.addLayer(new CHatLayerRenderer<>(playerRenderer));
            playerRenderer.addLayer(new AuraOfCulinaryArtsEnchantmentEffectRenderer<>(playerRenderer));
        }
        event.getEntityTypes().stream().map(event::getRenderer)
                .filter(renderer -> renderer instanceof LivingEntityRenderer<?, ?> livingRenderer && livingRenderer.getModel() instanceof HumanoidModel<?>)
                .forEach(renderer -> ((LivingEntityRenderer<?, ?>) renderer).addLayer(new CHatLayerRenderer(((LivingEntityRenderer<?, ?>) renderer))));
    }
}
