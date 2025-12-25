package mangopill.customized.integration.curios;

import mangopill.customized.Customized;
import mangopill.customized.common.util.CStringUtil;
import mangopill.customized.integration.ICompatModRegistry;
import mangopill.customized.integration.curios.client.renderer.CulinaryMastersHatRenderer;
import mangopill.customized.integration.curios.common.CuriosItemRegistry;
import mangopill.customized.integration.curios.common.item.CulinaryMastersHatCurio;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static mangopill.customized.common.util.CStringUtil.*;

public record CuriosRegistry() implements ICompatModRegistry {
    public static final CStringUtil.ComponentFactory C_CURIOS_TOOLTIP = (s, a) -> translate("curios" + "." + "tooltip" + "." + Customized.MODID + "." + s, a);
    public static final CStringUtil.ComponentFactory C_CURIOS_MESSAGE = (s, a) -> translate("curios" + "." + "message" + "." + Customized.MODID + "." + s, a);

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
