package mangopill.customized.common.util;

import mangopill.customized.Customized;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class TagUtil {
    private TagUtil() {
    }
    public static TagKey<Block> basicCBlockTag(String s) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", s));
    }

    public static TagKey<Item> basicCItemTag(String s) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", s));
    }

    public static TagKey<Item> basicModItemTag(String s) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Customized.MODID, s));
    }

    public static TagKey<Block> basicModBlockTag(String s) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Customized.MODID, s));
    }
}
