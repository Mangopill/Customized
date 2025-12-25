package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.CStringUtil.*;

public final class CCreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Customized.MODID);

    public static final Supplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TAB.register(Customized.MODID,
            () -> CreativeModeTab.builder()
                    .title(translate("itemGroup." + Customized.MODID))
                    .icon(() -> new ItemStack(CItemRegistry.CHEF_HAT.get()))
                    .displayItems((parameters, output) -> CItemRegistry.CREATIVE_MODE_TAB.forEach((item) -> output.accept(item.get())))
                    .build());
}
