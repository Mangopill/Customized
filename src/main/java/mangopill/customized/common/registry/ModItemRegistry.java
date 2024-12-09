package mangopill.customized.common.registry;

import com.google.common.collect.Sets;
import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateSlotRecord;
import mangopill.customized.common.item.SoupBowlItem;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModItemRegistry {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Customized.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_MODE_TAB = Sets.newLinkedHashSet();
    //item
    public static final Supplier<Item> ROCK_SUGAR = registerWithCreativeTab(
            "rock_sugar", basicItem());
    public static final Supplier<Item> SOILED_SEED = registerWithCreativeTab(
            "soiled_seed", basicItem());
    public static final Supplier<Item> KETCHUP = registerWithCreativeTab(
            "ketchup", drinkItem(FoodValue.KETCHUP));
    //block
    public static final Supplier<Item> SUSPICIOUS_DIRT = registerWithCreativeTab(
            "suspicious_dirt", blockItem(ModBlockRegistry.SUSPICIOUS_DIRT, basicItemProperties()));
    //crop
    public static final Supplier<Item> RICE = registerWithCreativeTab(
            "rice", basicItem());
    public static final Supplier<Item> RICE_SEED = registerWithCreativeTab(
            "rice_seed", itemNameBlockItem(ModBlockRegistry.RICE_CROP, basicItemProperties()));
    public static final Supplier<Item> TOMATO = registerWithCreativeTab(
            "tomato", basicFoodItem(FoodValue.TOMATO));
    public static final Supplier<Item> TOMATO_SEED = registerWithCreativeTab(
            "tomato_seed", itemNameBlockItem(ModBlockRegistry.TOMATO_CROP, basicItemProperties()));
    public static final Supplier<Item> BITTER_GOURD = registerWithCreativeTab(
            "bitter_gourd", basicFoodItem(FoodValue.BITTER_GOURD));
    public static final Supplier<Item> BITTER_GOURD_SEED = registerWithCreativeTab(
            "bitter_gourd_seed", itemNameBlockItem(ModBlockRegistry.BITTER_GOURD_CROP, basicItemProperties()));
    //kitchenware item
    public static final Supplier<Item> SPATULA = registerWithCreativeTab(
            "spatula", () -> new ShovelItem(Tiers.IRON, new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 2, -3.0F))));
    public static final Supplier<Item> CASSEROLE_ILD = registerWithCreativeTab(
            "casserole_lid", () -> new Item(basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> FAMOUS_DISH_PLATE = registerWithCreativeTab(
            "famous_dish_plate", () -> new Item(basicItemProperties()));
    //kitchenware block
    public static final Supplier<Item> CASSEROLE = registerWithCreativeTab(
            "casserole", blockItem(ModBlockRegistry.CASSEROLE, basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> SOUP_BOWL = registerWithCreativeTab(
            "soup_bowl", () -> new SoupBowlItem(ModBlockRegistry.SOUP_BOWL.get(), basicItemProperties().stacksTo(1).food(FoodValue.NULL)
                    .component(ModDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(PlateSlotRecord.SOUP_BOWL.ingredientInput() + PlateSlotRecord.SOUP_BOWL.seasoningInput() + 1)))));
}
