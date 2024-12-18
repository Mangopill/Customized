package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;
import static net.minecraft.world.level.block.Blocks.*;

public class ModBlockRegistry {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(Registries.BLOCK, Customized.MODID);
    //block
    public static final Supplier<Block> SUSPICIOUS_DIRT = BLOCK.register(
            "suspicious_dirt", () -> new ModBrushableBlock(DIRT, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED,
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.SNARE).strength(0.25F).sound(SoundType.SUSPICIOUS_GRAVEL).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> SALT_PAN = BLOCK.register(
            "salt_pan", () -> new SaltPanBlock(BlockBehaviour.Properties.ofFullCopy(OAK_PLANKS)));
    public static final Supplier<Block> TOMATO_AND_BEEF_BRISKET_SOUP = BLOCK.register(
            "tomato_and_beef_brisket_soup", () -> new FamousDishBlock(famousDishBlockProperties(), FoodValue.TOMATO_AND_BEEF_BRISKET_SOUP));
    public static final Supplier<Block> FISH_MAW_AND_CHICKEN_SOUP = BLOCK.register(
            "fish_maw_and_chicken_soup", () -> new FamousDishBlock(famousDishBlockProperties(), FoodValue.FISH_MAW_AND_CHICKEN_SOUP));
    public static final Supplier<Block> BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP = BLOCK.register(
            "braised_chicken_with_shiitake_mushrooms_soup", () -> new FamousDishBlock(famousDishBlockProperties(), FoodValue.BRAISED_CHICKEN_WITH_SHIITAKE_MUSHROOMS_SOUP));
    public static final Supplier<Block> RADISH_AND_PORK_RIB_SOUP = BLOCK.register(
            "radish_and_pork_rib_soup", () -> new FamousDishBlock(famousDishBlockProperties(), FoodValue.RADISH_AND_PORK_RIB_SOUP));
    //crop
    public static final Supplier<Block> RICE_CROP = BLOCK.register(
            "rice_crop", () -> new RiceCropBlock(cropBlockProperties()));
    public static final Supplier<Block> TOMATO_CROP = BLOCK.register(
            "tomato_crop", () -> new TomatoCropBlock(cropBlockProperties()));
    public static final Supplier<Block> BITTER_GOURD_CROP = BLOCK.register(
            "bitter_gourd_crop", () -> new BitterGourdCropBlock(cropBlockProperties()));
    public static final Supplier<Block> BROAD_BEAN_CROP = BLOCK.register(
            "broad_bean_crop", () -> new BroadBeanCropBlock(cropBlockProperties()));
    public static final Supplier<Block> CHILLI_CROP = BLOCK.register(
            "chilli_crop", () -> new ChilliCropBlock(cropBlockProperties()));
    public static final Supplier<Block> SOYBEAN_CROP = BLOCK.register(
            "soybean_crop", () -> new SoybeanCropBlock(cropBlockProperties()));
    public static final Supplier<Block> SICHUAN_PEPPER_CROP = BLOCK.register(
            "sichuan_pepper_crop", () -> new SichuanPepperCropBlock(cropBlockProperties()));
    public static final Supplier<Block> SCALLION_CROP = BLOCK.register(
            "scallion_crop", () -> new ScallionCropBlock(cropBlockProperties()));
    public static final Supplier<Block> GINGER_CROP = BLOCK.register(
            "ginger_crop", () -> new GingerCropBlock(cropBlockProperties()));
    //kitchenware block
    public static final Supplier<Block> CASSEROLE = BLOCK.register(
            "casserole", () -> new CasseroleBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(0.6F, 5.0F).sound(SoundType.DECORATED_POT)));
    public static final Supplier<Block> SOUP_BOWL = BLOCK.register(
            "soup_bowl", () -> new SoupBowlBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(0.3F, 2.0F).sound(SoundType.STONE)));
    public static final Supplier<Block> BREWING_BARREL = BLOCK.register(
            "brewing_barrel", () -> new BrewingBarrelBlock(Block.Properties.ofFullCopy(BARREL)));
}
