package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateRecord;
import mangopill.customized.common.item.*;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.RegistryUtil.*;

public final class CItemRegistry {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Customized.MODID);
    public static LinkedHashSet<Supplier<Item>> CREATIVE_MODE_TAB = new LinkedHashSet<>();
    // item
    public static final Supplier<Item> ROCK_SUGAR = registerWithCreativeTab(ITEM,
            "rock_sugar", basicItem());
    public static final Supplier<Item> KETCHUP = registerWithCreativeTab(ITEM,
            "ketchup", drinkItem(FoodValue.KETCHUP));
    public static final Supplier<Item> VINEGAR = registerWithCreativeTab(ITEM,
            "vinegar", drinkItem(FoodValue.VINEGAR));
    public static final Supplier<Item> DOUBAN = registerWithCreativeTab(ITEM,
            "douban", drinkItem(FoodValue.DOUBAN));
    public static final Supplier<Item> SALT = registerWithCreativeTab(ITEM,
            "salt", basicItem());
    public static final Supplier<Item> SOY_SAUCE = registerWithCreativeTab(ITEM,
            "soy_sauce", drinkItem(FoodValue.SOY_SAUCE));
    public static final Supplier<Item> CHICKEN_ESSENCE = registerWithCreativeTab(ITEM,
            "chicken_essence", basicItem());
    public static final Supplier<Item> OYSTER_SAUCE = registerWithCreativeTab(ITEM,
            "oyster_sauce", drinkItem(FoodValue.OYSTER_SAUCE));
    public static final Supplier<Item> SWEET_AND_SOUR_REFRESHING_SPICE = registerWithCreativeTab(ITEM,
            "sweet_and_sour_refreshing_spice", basicItem());
    public static final Supplier<Item> RICH_AND_NOURISHING_SPICE = registerWithCreativeTab(ITEM,
            "rich_and_nourishing_spice", basicItem());
    public static final Supplier<Item> LIGHT_AND_FRESH_AROMATIC_SPICE = registerWithCreativeTab(ITEM,
            "light_and_fresh_aromatic_spice", basicItem());
    public static final Supplier<Item> SOILED_SEED = registerWithCreativeTab(ITEM,
            "soiled_seed", basicItem());
    // crop
    public static final Supplier<Item> RICE = registerWithCreativeTab(ITEM,
            "rice", basicItem());
    public static final Supplier<Item> RICE_PANICLE = registerWithCreativeTab(ITEM,
            "rice_panicle", basicItem());
    public static final Supplier<Item> RICE_SEED = registerWithCreativeTab(ITEM,
            "rice_seed", itemNameBlockItem(CBlockRegistry.RICE_CROP, basicItemProperties()));
    public static final Supplier<Item> TOMATO = registerWithCreativeTab(ITEM,
            "tomato", basicFoodItem(FoodValue.TOMATO));
    public static final Supplier<Item> TOMATO_SEED = registerWithCreativeTab(ITEM,
            "tomato_seed", itemNameBlockItem(CBlockRegistry.TOMATO_CROP, basicItemProperties()));
    public static final Supplier<Item> BITTER_GOURD = registerWithCreativeTab(ITEM,
            "bitter_gourd", basicFoodItem(FoodValue.BITTER_GOURD));
    public static final Supplier<Item> BITTER_GOURD_SEED = registerWithCreativeTab(ITEM,
            "bitter_gourd_seed", itemNameBlockItem(CBlockRegistry.BITTER_GOURD_CROP, basicItemProperties()));
    public static final Supplier<Item> BROAD_BEAN = registerWithCreativeTab(ITEM,
            "broad_bean", foodSeedItem(CBlockRegistry.BROAD_BEAN_CROP, FoodValue.BROAD_BEAN));
    public static final Supplier<Item> CHILLI = registerWithCreativeTab(ITEM,
            "chilli", basicFoodItem(FoodValue.CHILLI));
    public static final Supplier<Item> CHILLI_SEED = registerWithCreativeTab(ITEM,
            "chilli_seed", itemNameBlockItem(CBlockRegistry.CHILLI_CROP, basicItemProperties()));
    public static final Supplier<Item> SOYBEAN = registerWithCreativeTab(ITEM,
            "soybean", foodSeedItem(CBlockRegistry.SOYBEAN_CROP, FoodValue.SOYBEAN));
    public static final Supplier<Item> SICHUAN_PEPPER = registerWithCreativeTab(ITEM,
            "sichuan_pepper", itemNameBlockItem(CBlockRegistry.SICHUAN_PEPPER_CROP, basicItemProperties()));
    public static final Supplier<Item> SCALLION = registerWithCreativeTab(ITEM,
            "scallion", foodSeedItem(CBlockRegistry.SCALLION_CROP, FoodValue.SCALLION));
    public static final Supplier<Item> GINGER = registerWithCreativeTab(ITEM,
            "ginger", foodSeedItem(CBlockRegistry.GINGER_CROP, FoodValue.GINGER));
    // fluid
    public static final Supplier<Item> SOUP_BUCKET = registerWithCreativeTab(ITEM,
            "soup_bucket", bucketItem(CFluidRegistry.SOUP));
    // kitchenware item
    public static final Supplier<Item> SPOON = registerWithCreativeTab(ITEM,
            "spoon", () -> new ShovelItem(Tiers.IRON, basicItemProperties().attributes(ShovelItem.createAttributes(Tiers.IRON, 2.0F, -3.0F))));
    public static final Supplier<Item> SPATULA = registerWithCreativeTab(ITEM,
            "spatula", () -> new ShovelItem(Tiers.IRON, basicItemProperties().attributes(ShovelItem.createAttributes(Tiers.IRON, 2.2F, -3.0F))));
    public static final Supplier<Item> CASSEROLE_LID = registerWithCreativeTab(ITEM,
            "casserole_lid", () -> new Item(basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> FAMOUS_DISH_PLATE = registerWithCreativeTab(ITEM,
            "famous_dish_plate", basicItem());
    public static final Supplier<Item> WOODEN_KNIFE = registerWithCreativeTab(ITEM,
            "wooden_knife", modKnifeItem(Tiers.WOOD, 0.03F));
    public static final Supplier<Item> STONE_KNIFE = registerWithCreativeTab(ITEM,
            "stone_knife", modKnifeItem(Tiers.STONE, 0.08F));
    public static final Supplier<Item> GOLDEN_KNIFE = registerWithCreativeTab(ITEM,
            "golden_knife", modKnifeItem(Tiers.GOLD, 0.15F));
    public static final Supplier<Item> IRON_KNIFE = registerWithCreativeTab(ITEM,
            "iron_knife", modKnifeItem(Tiers.IRON, 0.12F));
    public static final Supplier<Item> DIAMOND_KNIFE = registerWithCreativeTab(ITEM,
            "diamond_knife", modKnifeItem(Tiers.DIAMOND, 0.15F));
    public static final Supplier<Item> NETHERITE_KNIFE = registerWithCreativeTab(ITEM,
            "netherite_knife", modFireResistantKnifeItem(Tiers.NETHERITE, 0.2F));
    // famous dish
    public static final Supplier<Item> TOMATO_AND_BEEF_BRISKET_SOUP = registerWithCreativeTab(ITEM,
            "tomato_and_beef_brisket_soup", blockItem(CBlockRegistry.TOMATO_AND_BEEF_BRISKET_SOUP, basicItemProperties()));
    public static final Supplier<Item> FISH_MAW_AND_CHICKEN_SOUP = registerWithCreativeTab(ITEM,
            "fish_maw_and_chicken_soup", blockItem(CBlockRegistry.FISH_MAW_AND_CHICKEN_SOUP, basicItemProperties()));
    public static final Supplier<Item> BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP = registerWithCreativeTab(ITEM,
            "braised_chicken_with_shiitake_mushrooms_soup", blockItem(CBlockRegistry.BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP, basicItemProperties()));
    public static final Supplier<Item> RADISH_AND_PORK_RIB_SOUP = registerWithCreativeTab(ITEM,
            "radish_and_pork_rib_soup", blockItem(CBlockRegistry.RADISH_AND_PORK_RIB_SOUP, basicItemProperties()));
    // block
    public static final Supplier<Item> SUSPICIOUS_DIRT = registerWithCreativeTab(ITEM,
            "suspicious_dirt", blockItem(CBlockRegistry.SUSPICIOUS_DIRT, basicItemProperties()));
    public static final Supplier<Item> SALT_PAN = registerWithCreativeTab(ITEM,
            "salt_pan", blockItem(CBlockRegistry.SALT_PAN, basicItemProperties()));
    // kitchenware block
    public static final Supplier<Item> CASSEROLE = registerWithCreativeTab(ITEM,
            "casserole", blockItem(CBlockRegistry.CASSEROLE, basicItemProperties()));
    public static final Supplier<Item> ROASTER = registerWithCreativeTab(ITEM,
            "roaster", blockItem(CBlockRegistry.ROASTER, basicItemProperties()));
    public static final Supplier<Item> WOK = registerWithCreativeTab(ITEM,
            "wok", blockItem(CBlockRegistry.WOK, basicItemProperties()));
    public static final Supplier<Item> STEAMER = registerWithCreativeTab(ITEM,
            "steamer", blockItem(CBlockRegistry.STEAMER, basicItemProperties()));
    public static final Supplier<Item> SOUP_BOWL = registerWithCreativeTab(ITEM,
            "soup_bowl", () -> new SoupBowlItem(CBlockRegistry.SOUP_BOWL, basicPlateItemProperties(
                    PlateRecord.SOUP_BOWL.ingredientInput() + PlateRecord.SOUP_BOWL.seasoningInput() + PlateRecord.SOUP_BOWL.spiceInput())));
    public static final Supplier<Item> BAKING_PAN = registerWithCreativeTab(ITEM,
            "baking_pan", () -> new BakingPanItem(CBlockRegistry.BAKING_PAN, basicPlateItemProperties(
                    PlateRecord.BAKING_PAN.ingredientInput() + PlateRecord.BAKING_PAN.seasoningInput() + PlateRecord.BAKING_PAN.spiceInput())));
    public static final Supplier<Item> DISH = registerWithCreativeTab(ITEM,
            "dish", () -> new DishItem(CBlockRegistry.DISH, basicPlateItemProperties(
                    PlateRecord.DISH.ingredientInput() + PlateRecord.DISH.seasoningInput() + PlateRecord.DISH.spiceInput())));
    public static final Supplier<Item> PLATTER = registerWithCreativeTab(ITEM,
            "platter", () -> new PlatterItem(CBlockRegistry.PLATTER, basicPlateItemProperties(
                    PlateRecord.PLATTER.ingredientInput() + PlateRecord.PLATTER.seasoningInput() + PlateRecord.PLATTER.spiceInput())));
    public static final Supplier<Item> BREWING_BARREL = registerWithCreativeTab(ITEM,
            "brewing_barrel", blockItem(CBlockRegistry.BREWING_BARREL, basicItemProperties()));
    public static final Supplier<Item> CRATE = registerWithCreativeTab(ITEM,
            "crate", () -> new CrateItem(CBlockRegistry.CRATE, basicItemProperties()
                    .component(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(18))).stacksTo(1)));
    public static final Supplier<Item> CUTTING_BOARD = registerWithCreativeTab(ITEM,
            "cutting_board", blockItem(CBlockRegistry.CUTTING_BOARD, basicItemProperties()));
    // hat
    public static final Supplier<Item> CHEF_HAT = registerWithCreativeTab(ITEM,
            "chef_hat", modHatItem(CArmorMaterialRegistry.CHEF, Rarity.RARE, getCLoc("item/armor/chef_hat"), 10, -1.15D, 0.75F));
    public static final Supplier<Item> NETHERITE_CHEF_HAT = registerWithCreativeTab(ITEM,
            "netherite_chef_hat", modFireResistantHatItem(CArmorMaterialRegistry.NETHERITE_CHEF, Rarity.EPIC, getCLoc("item/armor/netherite_chef_hat"), 37, -1.0D, 1.0F));
}
