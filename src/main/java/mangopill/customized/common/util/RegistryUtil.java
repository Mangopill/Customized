package mangopill.customized.common.util;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.*;
import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashSet;
import java.util.function.*;

import static mangopill.customized.common.registry.CItemRegistry.*;

public final class RegistryUtil {
    private RegistryUtil() {}

    public static Item.Properties basicItemProperties() {
        return new Item.Properties();
    }

    public static Item.Properties basicPlateItemProperties(int slot) {
        return basicItemProperties().stacksTo(1).food(FoodValue.EMPTY)
                .component(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(new ItemStackHandler(slot)));
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

    public static Supplier<Item> bucketItem(Supplier<BaseFlowingFluid> fluid) {
        return () -> new CBucketItem(fluid, basicItemProperties().craftRemainder(Items.BUCKET).stacksTo(1));
    }

    public static Supplier<Item> modKnifeItem(Tier tier, float chanceLevel) {
        return () -> new KnifeItem(tier, basicItemProperties().attributes(SwordItem.createAttributes(tier, 1.5F, -2.1F)), chanceLevel);
    }

    public static Supplier<Item> modFireResistantKnifeItem(Tier tier, float chanceLevel) {
        return () -> new KnifeItem(tier, basicItemProperties().fireResistant().attributes(SwordItem.createAttributes(tier, 1.5F, -2.1F)), chanceLevel);
    }

    public static Supplier<Item> modHatItem(Holder<ArmorMaterial> material, Rarity rarity, ResourceLocation texture, int durabilityFactor, double translateY, float scale) {
        return () -> new CHatItem(material, ArmorItem.Type.HELMET, basicItemProperties().durability(ArmorItem.Type.HELMET.getDurability(durabilityFactor)).rarity(rarity), texture, translateY, scale);
    }

    public static Supplier<Item> modFireResistantHatItem(Holder<ArmorMaterial> material, Rarity rarity, ResourceLocation texture, int durabilityFactor, double translateY, float scale) {
        return () -> new CHatItem(material, ArmorItem.Type.HELMET, basicItemProperties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(durabilityFactor)).rarity(rarity), texture, translateY, scale);
    }

    public static Block.Properties basicBlockProperties() {
        return Block.Properties.of();
    }

    public static Block.Properties cropBlockProperties() {
        return Block.Properties.ofFullCopy(Blocks.WHEAT);
    }

    public static Block.Properties famousDishBlockProperties() {
        return basicBlockProperties().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.SNARE).strength(0.2F).sound(SoundType.STONE).pushReaction(PushReaction.BLOCK);
    }

    public static<T extends BlockEntity> Supplier<BlockEntityType<T>> basicBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntityType, Supplier<Block> supplier) {
        return () -> BlockEntityType.Builder.of(blockEntityType , supplier.get()).build(null);
    }

    public static ToIntFunction<BlockState> lidBlockEmission(int lightValue) {
        return state -> !state.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID) ? lightValue : 0;
    }

    public static Supplier<Item> registerWithCCreativeTab(final DeferredRegister.Items items, final String string, final Supplier<Item> supplier) {
        return registerWithCreativeTab(items, CREATIVE_MODE_TAB, string, supplier);
    }

    public static Supplier<Item> registerWithCreativeTab(final DeferredRegister.Items items, final LinkedHashSet<Supplier<Item>> hashSet, final String string, final Supplier<Item> supplier) {
        Supplier<Item> register = items.register(string, supplier);
        hashSet.add(register);
        return register;
    }

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String s) {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return Customized.MODID + ":" + s;
            }
        };
    }
}
