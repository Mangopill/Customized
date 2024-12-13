package mangopill.customized.common.registry;

import com.google.common.collect.Sets;
import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateSlotRecord;
import mangopill.customized.common.item.SoupBowlItem;
import net.minecraft.world.item.*;
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
    public static final Supplier<Item> BROAD_BEAN = registerWithCreativeTab(
            "broad_bean", foodSeedItem(ModBlockRegistry.BROAD_BEAN_CROP, FoodValue.BROAD_BEAN));
    public static final Supplier<Item> CHILLI = registerWithCreativeTab(
            "chilli", basicFoodItem(FoodValue.CHILLI));
    public static final Supplier<Item> CHILLI_SEED = registerWithCreativeTab(
            "chilli_seed", itemNameBlockItem(ModBlockRegistry.CHILLI_CROP, basicItemProperties()));
    public static final Supplier<Item> SOYBEAN = registerWithCreativeTab(
            "soybean", foodSeedItem(ModBlockRegistry.SOYBEAN_CROP, FoodValue.SOYBEAN));
    public static final Supplier<Item> SICHUAN_PEPPER = registerWithCreativeTab(
            "sichuan_pepper", itemNameBlockItem(ModBlockRegistry.SICHUAN_PEPPER_CROP, basicItemProperties()));
    public static final Supplier<Item> SCALLION = registerWithCreativeTab(
            "scallion", foodSeedItem(ModBlockRegistry.SCALLION_CROP, FoodValue.SCALLION));
    public static final Supplier<Item> GINGER = registerWithCreativeTab(
            "ginger", foodSeedItem(ModBlockRegistry.GINGER_CROP, FoodValue.GINGER));
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
            "soup_bowl", () -> new SoupBowlItem(ModBlockRegistry.SOUP_BOWL.get(), basicPlateItemProperties(
                    PlateSlotRecord.SOUP_BOWL.ingredientInput() + PlateSlotRecord.SOUP_BOWL.seasoningInput() + 1)));
    public static final Supplier<Item> BREWING_BARREL = registerWithCreativeTab(
            "brewing_barrel", blockItem(ModBlockRegistry.BREWING_BARREL, basicItemProperties().stacksTo(1)));
    /**
     * Just for the logo, no other purpose.
     */
    public static final Supplier<Item> CHEF_HAT = ITEM.register(
            "chef_hat", basicItem());
}
