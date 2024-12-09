package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Customized.MODID);

    public static final Supplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TAB.register(Customized.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.customized"))
                    .icon(() -> new ItemStack(ModItemRegistry.CHEF_HAT.get()))
                    .displayItems((parameters, output) -> ModItemRegistry.CREATIVE_MODE_TAB.forEach((item) -> output.accept(item.get())))
                    .build());
}
