package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.serializer.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CRecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Customized.MODID);

    public static final Supplier<RecipeSerializer<?>> PROPERTY_VALUE = RECIPE_SERIALIZER.register("property_value", PropertyValueSerializer::new);
    public static final Supplier<RecipeSerializer<?>> NUTRIENT_BUFF = RECIPE_SERIALIZER.register("nutrient_buff", NutrientBuffSerializer::new);
    public static final Supplier<RecipeSerializer<?>> NUTRIENT_CATEGORY = RECIPE_SERIALIZER.register("nutrient_category", NutrientCategorySerializer::new);
    public static final Supplier<RecipeSerializer<?>> CASSEROLE = RECIPE_SERIALIZER.register("casserole", CasseroleSerializer::new);
    public static final Supplier<RecipeSerializer<?>> ROASTER = RECIPE_SERIALIZER.register("roaster", RoasterSerializer::new);
    public static final Supplier<RecipeSerializer<?>> BREWING_BARREL = RECIPE_SERIALIZER.register("brewing_barrel", BrewingBarrelSerializer::new);
    public static final Supplier<RecipeSerializer<?>> CRATE = RECIPE_SERIALIZER.register("crate", CrateSerializer::new);
    public static final Supplier<RecipeSerializer<?>> CUTTING_BOARD = RECIPE_SERIALIZER.register("cutting_board", CuttingBoardSerializer::new);
}
