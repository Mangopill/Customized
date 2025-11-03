package mangopill.customized.common.registry;

import com.google.common.collect.Sets;
import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateSlotRecord;
import mangopill.customized.common.item.*;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class CItemRegistry {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Customized.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_MODE_TAB = Sets.newLinkedHashSet();
    //item
    public static final Supplier<Item> ROCK_SUGAR = registerWithCreativeTab(
            "rock_sugar", basicItem());
    public static final Supplier<Item> KETCHUP = registerWithCreativeTab(
            "ketchup", drinkItem(FoodValue.KETCHUP));
    public static final Supplier<Item> VINEGAR = registerWithCreativeTab(
            "vinegar", drinkItem(FoodValue.VINEGAR));
    public static final Supplier<Item> DOUBAN = registerWithCreativeTab(
            "douban", drinkItem(FoodValue.DOUBAN));
    public static final Supplier<Item> SALT = registerWithCreativeTab(
            "salt", basicItem());
    public static final Supplier<Item> SOY_SAUCE = registerWithCreativeTab(
            "soy_sauce", drinkItem(FoodValue.SOY_SAUCE));
    public static final Supplier<Item> CHICKEN_ESSENCE = registerWithCreativeTab(
            "chicken_essence", basicItem());
    public static final Supplier<Item> OYSTER_SAUCE = registerWithCreativeTab(
            "oyster_sauce", drinkItem(FoodValue.OYSTER_SAUCE));
    public static final Supplier<Item> SWEET_AND_SOUR_REFRESHING_SPICE = registerWithCreativeTab(
            "sweet_and_sour_refreshing_spice", basicItem());
    public static final Supplier<Item> RICH_AND_NOURISHING_SPICE = registerWithCreativeTab(
            "rich_and_nourishing_spice", basicItem());
    public static final Supplier<Item> LIGHT_AND_FRESH_AROMATIC_SPICE = registerWithCreativeTab(
            "light_and_fresh_aromatic_spice", basicItem());
    public static final Supplier<Item> SOILED_SEED = registerWithCreativeTab(
            "soiled_seed", basicItem());
    //crop
    public static final Supplier<Item> RICE = registerWithCreativeTab(
            "rice", basicItem());
    public static final Supplier<Item> RICE_SEED = registerWithCreativeTab(
            "rice_seed", itemNameBlockItem(CBlockRegistry.RICE_CROP, basicItemProperties()));
    public static final Supplier<Item> TOMATO = registerWithCreativeTab(
            "tomato", basicFoodItem(FoodValue.TOMATO));
    public static final Supplier<Item> TOMATO_SEED = registerWithCreativeTab(
            "tomato_seed", itemNameBlockItem(CBlockRegistry.TOMATO_CROP, basicItemProperties()));
    public static final Supplier<Item> BITTER_GOURD = registerWithCreativeTab(
            "bitter_gourd", basicFoodItem(FoodValue.BITTER_GOURD));
    public static final Supplier<Item> BITTER_GOURD_SEED = registerWithCreativeTab(
            "bitter_gourd_seed", itemNameBlockItem(CBlockRegistry.BITTER_GOURD_CROP, basicItemProperties()));
    public static final Supplier<Item> BROAD_BEAN = registerWithCreativeTab(
            "broad_bean", foodSeedItem(CBlockRegistry.BROAD_BEAN_CROP, FoodValue.BROAD_BEAN));
    public static final Supplier<Item> CHILLI = registerWithCreativeTab(
            "chilli", basicFoodItem(FoodValue.CHILLI));
    public static final Supplier<Item> CHILLI_SEED = registerWithCreativeTab(
            "chilli_seed", itemNameBlockItem(CBlockRegistry.CHILLI_CROP, basicItemProperties()));
    public static final Supplier<Item> SOYBEAN = registerWithCreativeTab(
            "soybean", foodSeedItem(CBlockRegistry.SOYBEAN_CROP, FoodValue.SOYBEAN));
    public static final Supplier<Item> SICHUAN_PEPPER = registerWithCreativeTab(
            "sichuan_pepper", itemNameBlockItem(CBlockRegistry.SICHUAN_PEPPER_CROP, basicItemProperties()));
    public static final Supplier<Item> SCALLION = registerWithCreativeTab(
            "scallion", foodSeedItem(CBlockRegistry.SCALLION_CROP, FoodValue.SCALLION));
    public static final Supplier<Item> GINGER = registerWithCreativeTab(
            "ginger", foodSeedItem(CBlockRegistry.GINGER_CROP, FoodValue.GINGER));
    //kitchenware item
    public static final Supplier<Item> SPOON = registerWithCreativeTab(
            "spoon", () -> new ShovelItem(Tiers.IRON, new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 2, -3.0F))));
    public static final Supplier<Item> CASSEROLE_ILD = registerWithCreativeTab(
            "casserole_lid", () -> new Item(basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> FAMOUS_DISH_PLATE = registerWithCreativeTab(
            "famous_dish_plate", basicItem());
    public static final Supplier<Item> WOODEN_KNIFE = registerWithCreativeTab(
            "wooden_knife", modKnifeItem(Tiers.WOOD, 0.03F));
    public static final Supplier<Item> STONE_KNIFE = registerWithCreativeTab(
            "stone_knife", modKnifeItem(Tiers.STONE, 0.08F));
    public static final Supplier<Item> GOLDEN_KNIFE = registerWithCreativeTab(
            "golden_knife", modKnifeItem(Tiers.GOLD, 0.15F));
    public static final Supplier<Item> IRON_KNIFE = registerWithCreativeTab(
            "iron_knife", modKnifeItem(Tiers.IRON, 0.12F));
    public static final Supplier<Item> DIAMOND_KNIFE = registerWithCreativeTab(
            "diamond_knife", modKnifeItem(Tiers.DIAMOND, 0.15F));
    public static final Supplier<Item> NETHERITE_KNIFE = registerWithCreativeTab(
            "netherite_knife", modKnifeItem(Tiers.NETHERITE, 0.2F));
    //famous dish
    public static final Supplier<Item> TOMATO_AND_BEEF_BRISKET_SOUP = registerWithCreativeTab(
            "tomato_and_beef_brisket_soup", blockItem(CBlockRegistry.TOMATO_AND_BEEF_BRISKET_SOUP, basicItemProperties()));
    public static final Supplier<Item> FISH_MAW_AND_CHICKEN_SOUP = registerWithCreativeTab(
            "fish_maw_and_chicken_soup", blockItem(CBlockRegistry.FISH_MAW_AND_CHICKEN_SOUP, basicItemProperties()));
    public static final Supplier<Item> BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP = registerWithCreativeTab(
            "braised_chicken_with_shiitake_mushrooms_soup", blockItem(CBlockRegistry.BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP, basicItemProperties()));
    public static final Supplier<Item> RADISH_AND_PORK_RIB_SOUP = registerWithCreativeTab(
            "radish_and_pork_rib_soup", blockItem(CBlockRegistry.RADISH_AND_PORK_RIB_SOUP, basicItemProperties()));
    //block
    public static final Supplier<Item> SUSPICIOUS_DIRT = registerWithCreativeTab(
            "suspicious_dirt", blockItem(CBlockRegistry.SUSPICIOUS_DIRT, basicItemProperties()));
    public static final Supplier<Item> SALT_PAN = registerWithCreativeTab(
            "salt_pan", blockItem(CBlockRegistry.SALT_PAN, basicItemProperties()));
    //kitchenware block
    public static final Supplier<Item> CASSEROLE = registerWithCreativeTab(
            "casserole", blockItem(CBlockRegistry.CASSEROLE, basicItemProperties()));
    public static final Supplier<Item> SOUP_BOWL = registerWithCreativeTab(
            "soup_bowl", () -> new SoupBowlItem(CBlockRegistry.SOUP_BOWL, basicPlateItemProperties(
                    PlateSlotRecord.SOUP_BOWL.ingredientInput() + PlateSlotRecord.SOUP_BOWL.seasoningInput() + 1)));
    public static final Supplier<Item> BREWING_BARREL = registerWithCreativeTab(
            "brewing_barrel", blockItem(CBlockRegistry.BREWING_BARREL, basicItemProperties()));
    public static final Supplier<Item> CRATE = registerWithCreativeTab(
            "crate", () -> new CrateItem(CBlockRegistry.CRATE, basicItemProperties()
                    .component(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(18))).stacksTo(1)));
    public static final Supplier<Item> CUTTING_BOARD = registerWithCreativeTab(
            "cutting_board", blockItem(CBlockRegistry.CUTTING_BOARD, basicItemProperties()));
    //hat
    public static final Supplier<Item> CHEF_HAT = registerWithCreativeTab(
            "chef_hat", modHatItem(CArmorMaterialRegistry.CHEF, Rarity.RARE, 10, -1.15D, 0.75F));
    public static final Supplier<Item> NETHERITE_CHEF_HAT = registerWithCreativeTab(
            "netherite_chef_hat", modHatItem(CArmorMaterialRegistry.NETHERITE_CHEF, Rarity.EPIC, 37, -1.0D, 1.0F));
}
