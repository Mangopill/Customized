package mangopill.customized.integration.curios;

import mangopill.customized.integration.curios.client.renderer.CulinaryMastersHatRenderer;
import mangopill.customized.integration.curios.common.CuriosItemRegistry;
import mangopill.customized.integration.curios.common.item.CulinaryMastersHatItem;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRegistry {
    public static void commonSetup(final FMLCommonSetupEvent event) {
        CuriosApi.registerCurio(CuriosItemRegistry.CULINARY_MASTERS_HAT.get(), new CulinaryMastersHatItem());
    }
    public static void clientSetup(final FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(CuriosItemRegistry.CULINARY_MASTERS_HAT.get(), CulinaryMastersHatRenderer::new);
    }
}
