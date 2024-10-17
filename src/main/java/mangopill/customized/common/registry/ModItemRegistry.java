package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Customized.MODID);

    public static final Supplier<BlockItem> SUSPICIOUS_DIRT = ITEMS.registerSimpleBlockItem(
            "suspicious_dirt",
            ModBlcokRegistry.SUSPICIOUS_DIRT, new Item.Properties());
}
