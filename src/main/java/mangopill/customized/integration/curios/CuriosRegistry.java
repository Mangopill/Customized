package mangopill.customized.integration.curios;

import mangopill.customized.integration.ICompatModRegistry;
import mangopill.customized.integration.curios.client.renderer.CulinaryMastersHatRenderer;
import mangopill.customized.integration.curios.common.CuriosItemRegistry;
import mangopill.customized.integration.curios.common.item.CulinaryMastersHatCurio;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public record CuriosRegistry() implements ICompatModRegistry {
    @Override
    public void registerCompat(IEventBus modBus, ModContainer container) {
        modBus.addListener(CuriosRegistry::commonSetup);
        CuriosItemRegistry.CURIOS_ITEM.register(modBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(CuriosRegistry::clientSetup);
        }
    }

    public static void commonSetup(final FMLCommonSetupEvent event) {
        CuriosApi.registerCurio(CuriosItemRegistry.CULINARY_MASTERS_HAT.get(), new CulinaryMastersHatCurio());
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(CuriosItemRegistry.CULINARY_MASTERS_HAT.get(), CulinaryMastersHatRenderer::new);
    }
}
