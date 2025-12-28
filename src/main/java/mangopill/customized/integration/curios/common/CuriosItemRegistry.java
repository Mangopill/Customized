package mangopill.customized.integration.curios.common;

import mangopill.customized.Customized;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public final class CuriosItemRegistry {
    public static final DeferredRegister.Items CURIOS_ITEM = DeferredRegister.createItems(Customized.MODID);
    // curio
    public static final Supplier<Item> CULINARY_MASTERS_HAT = registerWithCCreativeTab(CURIOS_ITEM,
            "culinary_masters_hat", () -> new Item(basicItemProperties().stacksTo(1).rarity(Rarity.EPIC)));
}
