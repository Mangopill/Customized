package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, Customized.MODID);

    public static final Supplier<RecipeType<PropertyValueRecipe>> PROPERTY_VALUE = RECIPE_TYPE.register("property_value", () -> registerRecipeType("property_value"));
    public static final Supplier<RecipeType<CasseroleRecipe>> CASSEROLE = RECIPE_TYPE.register("casserole", () -> registerRecipeType("casserole"));
}
