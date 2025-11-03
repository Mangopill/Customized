package mangopill.customized.common.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static mangopill.customized.common.util.TagUtil.*;

public final class ModTag {
    private ModTag() {
    }

    public static final TagKey<Item> SOILED_SEED = basicModItemTag("mod_seeds/soiled_seed");
    public static final TagKey<Item> SEASONING = basicModItemTag("seasoning");
    public static final TagKey<Item> FAMOUS_SPICE = basicModItemTag("famous_spice");
    public static final TagKey<Item> SPATULA = basicModItemTag("tools/spatula");
    public static final TagKey<Item> HAT = basicModItemTag("hat");

    public static final TagKey<Block> HEAT_SOURCE = basicModBlockTag("heat_source");
}
