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
    public static final TagKey<Item> KNIFE = basicModItemTag("tools/knife");
    public static final TagKey<Item> POT = basicModItemTag("pot");
    public static final TagKey<Item> CUSTOMIZED_PLATE = basicModItemTag("customized_plate");

    public static final TagKey<Block> HEAT_SOURCE = basicModBlockTag("heat_source");
}
