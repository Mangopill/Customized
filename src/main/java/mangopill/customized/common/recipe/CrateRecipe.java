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

public class CrateRecipe implements CRecipeInterface<RecipeWrapper> {
    private final NonNullList<Ingredient> ingredientItem;
    private final ItemStack output;
    private final int cookingTime;
    private final int ingredientInput;

    public CrateRecipe(NonNullList<Ingredient> ingredientItem, ItemStack output, int cookingTime) {
        this.ingredientItem = ingredientItem;
        this.output = output;
        this.cookingTime = cookingTime;
        ingredientInput = 9;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        return ingredient.size() == ingredientItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null;
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return this.output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredientItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.CRATE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.CRATE.get();
    }

    public NonNullList<Ingredient> getIngredientItem() {
        return ingredientItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getIngredientInput() {
        return ingredientInput;
    }
}
