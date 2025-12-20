package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

public record BrewingBarrelRecipe(NonNullList<Ingredient> ingredientItem, Ingredient containerItem, ItemStack output, int cookingTime) implements CRecipeInterface<RecipeWrapper> {
    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, 4);
        return ingredient.size() == ingredientItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null;
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> combinedIngredient = NonNullList.create();
        combinedIngredient.addAll(ingredientItem);
        combinedIngredient.add(containerItem);
        return combinedIngredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.BREWING_BARREL.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.BREWING_BARREL.get();
    }
}
