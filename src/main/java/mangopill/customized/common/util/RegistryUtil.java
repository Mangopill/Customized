package mangopill.customized.common.util;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.registry.ModDataComponentRegistry;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Supplier;

import static mangopill.customized.common.registry.ModItemRegistry.CREATIVE_MODE_TAB;
import static mangopill.customized.common.registry.ModItemRegistry.ITEM;

public final class RegistryUtil {
    private RegistryUtil() {
    }
    public static Item.Properties basicItemProperties() {
        return new Item.Properties();
    }

    public static Item.Properties basicPlateItemProperties(int slot) {
        return basicItemProperties().stacksTo(1).food(FoodValue.NULL)
                .component(ModDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(slot)));
    }

    public static Supplier<Item> basicItem() {
        return () -> new Item(basicItemProperties());
    }

    public static Supplier<Item> basicFoodItem(FoodProperties foodProperties) {
        return () -> new Item(basicItemProperties().food(foodProperties));
    }

    public static Supplier<Item> drinkItem(FoodProperties foodProperties) {
        return () -> new Item(basicItemProperties().food(foodProperties).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));
    }

    public static Supplier<Item> blockItem(Supplier<Block> supplier, Item.Properties properties) {
        return () -> new BlockItem(supplier.get(), properties);
    }

    public static Supplier<Item> itemNameBlockItem(Supplier<Block> supplier, Item.Properties properties) {
        return () -> new ItemNameBlockItem(supplier.get(), properties);
    }

    public static Supplier<Item> foodSeedItem(Supplier<Block> supplier, FoodProperties foodProperties) {
        return itemNameBlockItem(supplier, basicItemProperties().food(foodProperties));
    }

    public static Block.Properties cropBlockProperties() {
        return Block.Properties.ofFullCopy(Blocks.WHEAT);
    }

    public static Block.Properties famousDishBlockProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.SNARE).strength(0.2F).sound(SoundType.STONE).pushReaction(PushReaction.BLOCK);
    }

    public static<T extends BlockEntity> Supplier<BlockEntityType<T>> basicBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntityType, Supplier<Block> supplier) {
        return () -> BlockEntityType.Builder.of(blockEntityType , supplier.get()).build(null);
    }

    public static Supplier<Item> registerWithCreativeTab(final String string, final Supplier<Item> supplier) {
        Supplier<Item> register = ITEM.register(string, supplier);
        CREATIVE_MODE_TAB.add(register);
        return register;
    }

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String s) {
        return new RecipeType<>()
        {
            public String toString() {
                return Customized.MODID + ":" + s;
            }
        };
    }
}
