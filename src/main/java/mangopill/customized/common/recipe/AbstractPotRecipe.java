package mangopill.customized.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractPotRecipe implements ModRecipeInterface<RecipeWrapper> {

    private final NonNullList<Ingredient> ingredientItem;
    private final NonNullList<Ingredient> seasoningItem;
    private final Ingredient spiceItem;
    private final ItemStack output;
    private final int cookingTime;
    private final RecipeSerializer<?> recipeSerializer;
    private final RecipeType<?> recipeType;
    private final int ingredientInput;
    private final int seasoningInput;

    public AbstractPotRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem, Ingredient spiceItem, ItemStack output, int cookingTime, RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType,int ingredientSlot,int seasoningSlot) {
        this.ingredientItem = ingredientItem;
        this.seasoningItem = seasoningItem;
        this.spiceItem = spiceItem;
        this.output = output;
        this.cookingTime = cookingTime;
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        ingredientInput = ingredientSlot;
        seasoningInput = seasoningSlot;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper recipeWrapper, @NotNull Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        List<ItemStack> seasoning = getListByWrapper(recipeWrapper, ingredientInput, ingredientInput + seasoningInput);;
        return ItemStack.isSameItem(spiceItem.getItems()[0], recipeWrapper.getItem(ingredientInput + seasoningInput))
                && ingredient.size() == ingredientItem.size()
                && seasoning.size() == seasoningItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null
                && RecipeMatcher.findMatches(seasoning, seasoningItem) != null;
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
        NonNullList<Ingredient> combinedIngredient = NonNullList.create();
        combinedIngredient.addAll(ingredientItem);
        combinedIngredient.addAll(seasoningItem);
        if (spiceItem != null && !spiceItem.isEmpty()) {
            combinedIngredient.add(spiceItem);
        }
        return combinedIngredient;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return recipeSerializer;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return recipeType;
    }

    public NonNullList<Ingredient> getIngredientItem() {
        return ingredientItem;
    }

    public NonNullList<Ingredient> getSeasoningItem() {
        return seasoningItem;
    }

    public Ingredient getSpiceItem() {
        return spiceItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}
