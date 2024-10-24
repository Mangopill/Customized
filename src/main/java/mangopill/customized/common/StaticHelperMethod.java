package mangopill.customized.common;

import mangopill.customized.Customized;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static mangopill.customized.common.registry.ModItemRegistry.CREATIVE_MODE_TAB;
import static mangopill.customized.common.registry.ModItemRegistry.ITEMS;

public class StaticHelperMethod {
    public static Item.Properties basicItemProperties() {
        return new Item.Properties();
    }

    public static Supplier<Item> blockItem(Supplier<Block> supplier, Item.Properties properties) {
        return () -> new BlockItem(supplier.get(), properties);
    }

    public static<T extends BlockEntity> Supplier<BlockEntityType<T>> basicBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntitytType, Supplier<Block> supplier) {
        return () -> BlockEntityType.Builder.of(blockEntitytType , supplier.get()).build(null);
    }

    public static Supplier<Item> registerWithCreativeTab(final String string, final Supplier<Item> supplier) {
        Supplier<Item> register = ITEMS.register(string, supplier);
        CREATIVE_MODE_TAB.add(register);
        return register;
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
