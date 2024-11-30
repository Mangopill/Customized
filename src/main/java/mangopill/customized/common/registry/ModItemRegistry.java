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
    public static final Supplier<Item> SOILED_SEED = registerWithCreativeTab(
            "soiled_seed", () -> new Item(basicItemProperties()));
    //block
    public static final Supplier<Item> SUSPICIOUS_DIRT = registerWithCreativeTab(
            "suspicious_dirt", blockItem(ModBlcokRegistry.SUSPICIOUS_DIRT, basicItemProperties()));
    //kitchenware item
    public static final Supplier<Item> SPATULA = registerWithCreativeTab(
            "spatula", () -> new ShovelItem(Tiers.IRON, new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 2, -3.0F))));
    public static final Supplier<Item> CASSEROLE_ILD = registerWithCreativeTab(
            "casserole_lid", () -> new Item(basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> FAMOUS_DISH_PLATE = registerWithCreativeTab(
            "famous_dish_plate", () -> new Item(basicItemProperties()));
    //kitchenware block
    public static final Supplier<Item> CASSEROLE = registerWithCreativeTab(
            "casserole", blockItem(ModBlcokRegistry.CASSEROLE, basicItemProperties().stacksTo(1)));
    public static final Supplier<Item> SOUP_BOWL = registerWithCreativeTab(
            "soup_bowl", () -> new SoupBowlItem(ModBlcokRegistry.SOUP_BOWL.get(), basicItemProperties().stacksTo(1).food(FoodValue.NULL)
                    .component(ModDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(PlateSlotRecord.SOUP_BOWL.ingredientInput() + PlateSlotRecord.SOUP_BOWL.seasoningInput() + 1)))));
}
