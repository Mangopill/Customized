package mangopill.customized.common.util;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.KnifeItem;
import mangopill.customized.common.item.CHatItem;
import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static mangopill.customized.common.registry.CItemRegistry.*;

public final class RegistryUtil {
    private RegistryUtil() {
    }

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

    public static Block.Properties cropBlockProperties() {
        return Block.Properties.ofFullCopy(Blocks.WHEAT);
    }

    public static Block.Properties famousDishBlockProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.SNARE).strength(0.2F).sound(SoundType.STONE).pushReaction(PushReaction.BLOCK);
    }

    public static<T extends BlockEntity> Supplier<BlockEntityType<T>> basicBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntityType, Supplier<Block> supplier) {
        return () -> BlockEntityType.Builder.of(blockEntityType , supplier.get()).build(null);
    }

    public static ToIntFunction<BlockState> lidBlockEmission(int lightValue) {
        return state -> !state.getValue(AbstractPotBlock.LID).equals(PotState.WITHOUT_LID) ? lightValue : 0;
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
