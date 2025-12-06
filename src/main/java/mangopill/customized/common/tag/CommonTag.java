package mangopill.customized.common.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static mangopill.customized.common.util.TagUtil.*;

public final class CommonTag {
    private CommonTag() {
    }

    public static final TagKey<Item> CREEPER_IGNITERS = basicCommonItemTag("creeper_igniters");
}
