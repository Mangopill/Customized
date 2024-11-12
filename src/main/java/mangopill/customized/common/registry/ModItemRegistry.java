package mangopill.customized.common.registry;

import com.google.common.collect.Sets;
import mangopill.customized.Customized;
import mangopill.customized.common.item.SoiledSeedItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Customized.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_MODE_TAB = Sets.newLinkedHashSet();

    public static final Supplier<Item> SUSPICIOUS_DIRT = registerWithCreativeTab(
            "suspicious_dirt", blockItem(ModBlcokRegistry.SUSPICIOUS_DIRT, basicItemProperties()));
    public static final Supplier<Item> SOILED_SEED = registerWithCreativeTab(
            "soiled_seed", () -> new SoiledSeedItem(basicItemProperties()));
}
