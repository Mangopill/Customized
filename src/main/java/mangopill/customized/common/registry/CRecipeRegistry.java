package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class CRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, Customized.MODID);

    public static final Supplier<RecipeType<PropertyValueRecipe>> PROPERTY_VALUE = RECIPE_TYPE.register("property_value", () -> registerRecipeType("property_value"));
    public static final Supplier<RecipeType<NutrientBuffRecipe>> NUTRIENT_BUFF = RECIPE_TYPE.register("nutrient_buff", () -> registerRecipeType("nutrient_buff"));
    public static final Supplier<RecipeType<NutrientCategoryRecipe>> NUTRIENT_CATEGORY = RECIPE_TYPE.register("nutrient_category", () -> registerRecipeType("nutrient_category"));
    public static final Supplier<RecipeType<CasseroleRecipe>> CASSEROLE = RECIPE_TYPE.register("casserole", () -> registerRecipeType("casserole"));
    public static final Supplier<RecipeType<RoasterRecipe>> ROASTER = RECIPE_TYPE.register("roaster", () -> registerRecipeType("roaster"));
    public static final Supplier<RecipeType<BrewingBarrelRecipe>> BREWING_BARREL = RECIPE_TYPE.register("brewing_barrel", () -> registerRecipeType("brewing_barrel"));
    public static final Supplier<RecipeType<CrateRecipe>> CRATE = RECIPE_TYPE.register("crate", () -> registerRecipeType("crate"));
    public static final Supplier<RecipeType<CuttingBoardRecipe>> CUTTING_BOARD = RECIPE_TYPE.register("cutting_board", () -> registerRecipeType("cutting_board"));
}
