package mangopill.customized.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static mangopill.customized.common.util.StringUtil.*;

public final class TagUtil {
    private TagUtil() {
    }

    public static TagKey<Block> basicCommonBlockTag(String string) {
        return BlockTags.create(ResourceLocation.withDefaultNamespace(string));
    }

    public static TagKey<Item> basicCommonItemTag(String string) {
        return ItemTags.create(ResourceLocation.withDefaultNamespace(string));
    }

    public static TagKey<Fluid> basicCommonFluidTag(String string) {
        return FluidTags.create(ResourceLocation.withDefaultNamespace(string));
    }

    public static TagKey<Block> basicCBlockTag(String string) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", string));
    }

    public static TagKey<Item> basicCItemTag(String string) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", string));
    }

    public static TagKey<Fluid> basicCFluidTag(String string) {
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", string));
    }

    public static TagKey<Item> basicModItemTag(String string) {
        return ItemTags.create(getCLoc(string));
    }

    public static TagKey<Block> basicModBlockTag(String string) {
        return BlockTags.create(getCLoc(string));
    }

    public static TagKey<Fluid> basicModFluidTag(String string) {
        return FluidTags.create(getCLoc(string));
    }
}
