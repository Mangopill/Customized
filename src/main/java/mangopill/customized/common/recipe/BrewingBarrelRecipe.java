package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.registry.ModRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrewingBarrelRecipe implements ModRecipeInterface<RecipeWrapper>{
    private final NonNullList<Ingredient> ingredientItem;
    private final Ingredient containerItem;
    private final ItemStack output;
    private final int cookingTime;
    private final int ingredientInput;

    public BrewingBarrelRecipe(NonNullList<Ingredient> ingredientItem, Ingredient containerItem, ItemStack output, int cookingTime) {
        this.ingredientItem = ingredientItem;
        this.containerItem = containerItem;
        this.output = output;
        this.cookingTime = cookingTime;
        ingredientInput = 4;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper recipeWrapper, @NotNull Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        return ingredient.size() == ingredientItem.size() && RecipeMatcher.findMatches(ingredient, ingredientItem) != null;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper recipeWrapper, HolderLookup.@NotNull Provider provider) {
        return this.output.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.output;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredientItem;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializerRegistry.BREWING_BARREL.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeRegistry.BREWING_BARREL.get();
    }

    public NonNullList<Ingredient> getIngredientItem() {
        return ingredientItem;
    }

    public Ingredient getContainerItem() {
        return containerItem;
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
