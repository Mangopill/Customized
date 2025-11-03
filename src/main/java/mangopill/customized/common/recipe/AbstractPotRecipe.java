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
import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public abstract class AbstractPotRecipe implements CRecipeInterface<RecipeWrapper> {
    private final NonNullList<Ingredient> ingredientItem;
    private final NonNullList<Ingredient> seasoningItem;
    private final Ingredient spiceItem;
    private final Ingredient containerItem;
    private final ItemStack output;
    private final int cookingTime;
    private final RecipeSerializer<?> recipeSerializer;
    private final RecipeType<?> recipeType;
    private final int ingredientInput;
    private final int seasoningInput;

    public AbstractPotRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem,
                             Ingredient spiceItem, Ingredient containerItem, ItemStack output,
                             int cookingTime, RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType,
                             int ingredientSlot, int seasoningSlot) {
        this.ingredientItem = ingredientItem;
        this.seasoningItem = seasoningItem;
        this.spiceItem = spiceItem;
        this.containerItem = containerItem;
        this.output = output;
        this.cookingTime = cookingTime;
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        ingredientInput = ingredientSlot;
        seasoningInput = seasoningSlot;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        List<ItemStack> seasoning = getListByWrapper(recipeWrapper, ingredientInput, ingredientInput + seasoningInput);
        ItemStack spice = recipeWrapper.getItem(ingredientInput + seasoningInput);
        return ingredient.size() == ingredientItem.size()
                && seasoning.size() == seasoningItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null
                && RecipeMatcher.findMatches(seasoning, seasoningItem) != null
                && containsSameItem(List.of(spiceItem.getItems()), spice);
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
        NonNullList<Ingredient> combinedIngredient = NonNullList.create();
        combinedIngredient.addAll(ingredientItem);
        combinedIngredient.addAll(seasoningItem);
        combinedIngredient.add(spiceItem);
        combinedIngredient.add(containerItem);
        return combinedIngredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return recipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
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

    public Ingredient getContainerItem() {
        return containerItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}
