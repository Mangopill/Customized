package mangopill.customized.integration.jei.ingredient;

import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import mezz.jei.api.ingredients.*;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class NutrientIngredientHelper implements IIngredientHelper<NutrientCategoryRecipe> {
    @Override
    public IIngredientType<NutrientCategoryRecipe> getIngredientType() {
        return NUTRIENT_INGREDIENT;
    }

    @Override
    public String getDisplayName(NutrientCategoryRecipe recipe) {
        return getPropertyNutrientCategoryComponent(recipe.name()).getString();
    }

    @Override
    public Iterable<Integer> getColors(NutrientCategoryRecipe recipe) {
        return Collections.singleton(recipe.getColorWithAlpha());
    }

    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(NutrientCategoryRecipe recipe, UidContext context) {
        return recipe.name();
    }

    @Override
    public ResourceLocation getResourceLocation(NutrientCategoryRecipe recipe) {
        return getCLoc(recipe.name());
    }

    @Override
    public NutrientCategoryRecipe copyIngredient(NutrientCategoryRecipe recipe) {
        return new NutrientCategoryRecipe(recipe.icon(), recipe.name(), recipe.color(), recipe.nutrition(), recipe.saturation());
    }

    @Override
    public String getErrorInfo(@Nullable NutrientCategoryRecipe recipe) {
        if (recipe == null) {
            return "Null nutrient category recipe";
        }
        if (recipe.name().isEmpty()) {
            return "Nutrient category recipe with empty name";
        }
        return "Nutrient category: " + recipe.name();
    }
}
