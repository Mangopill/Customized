package mangopill.customized.integration.curios.common;

import mangopill.customized.Customized;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.registry.CItemRegistry.CREATIVE_MODE_TAB;
import static mangopill.customized.common.util.RegistryUtil.basicItemProperties;

public class CuriosItemRegistry {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Customized.MODID);
    //curio
    public static final Supplier<Item> CULINARY_MASTERS_HAT = registerWithCreativeTab(
            "culinary_masters_hat", () -> new Item(basicItemProperties().stacksTo(1).rarity(Rarity.EPIC)));

    public static Supplier<Item> registerWithCreativeTab(final String string, final Supplier<Item> supplier) {
        Supplier<Item> register = ITEM.register(string, supplier);
        CREATIVE_MODE_TAB.add(register);
        return register;
    }
}
