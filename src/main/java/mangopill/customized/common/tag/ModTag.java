package mangopill.customized.common.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static mangopill.customized.common.util.TagUtil.*;

public class ModTag {
    public static final TagKey<Item> MOD_SEEDS_COMMON = basicModItemTag("mod_seeds/common");
    public static final TagKey<Item> MOD_SEEDS_SPECIAL = basicModItemTag("mod_seeds/special");
}
