package mangopill.customized.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import java.util.List;

import static mangopill.customized.common.block.entity.AbstractPotBlockEntity.*;

public abstract class AbstractPotRecipe implements CRecipeInterface<RecipeWrapper> {
    private final NonNullList<Ingredient> ingredientItem;
    private final NonNullList<Ingredient> seasoningItem;
    private final NonNullList<Ingredient> spiceItem;
    private final Ingredient containerItem;
    private final FluidIngredient fluidIngredient;
    private final ItemStack output;
    private final int cookingTime;
    private final boolean heated;
    private final RecipeSerializer<?> recipeSerializer;
    private final RecipeType<?> recipeType;
    private final int ingredientInput;
    private final int seasoningInput;

    public AbstractPotRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem,
                             NonNullList<Ingredient> spiceItem, Ingredient containerItem, FluidIngredient fluidIngredient, ItemStack output,
                             int cookingTime, boolean heated, RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType,
                             int ingredientSlot, int seasoningSlot) {
        this.ingredientItem = ingredientItem;
        this.seasoningItem = seasoningItem;
        this.spiceItem = spiceItem;
        this.containerItem = containerItem;
        this.fluidIngredient = fluidIngredient;
        this.output = output;
        this.cookingTime = cookingTime;
        this.heated = heated;
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        this.ingredientInput = ingredientSlot;
        this.seasoningInput = seasoningSlot;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        List<ItemStack> seasoning = getListByWrapper(recipeWrapper, ingredientInput, ingredientInput + seasoningInput);
        List<ItemStack> spice = getListByWrapper(recipeWrapper, ingredientInput + seasoningInput, ingredientInput + seasoningInput + OUTPUT);
        return ingredient.size() == ingredientItem.size()
                && seasoning.size() == seasoningItem.size()
                && spice.size() == spiceItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null
                && RecipeMatcher.findMatches(seasoning, seasoningItem) != null
                && RecipeMatcher.findMatches(spice, spiceItem) != null;
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
        combinedIngredient.addAll(seasoningItem);
        combinedIngredient.addAll(spiceItem);
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

    public NonNullList<Ingredient> getSpiceItem() {
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

    public FluidIngredient getFluidIngredient() {
        return fluidIngredient;
    }

    public boolean isHeated() {
        return heated;
    }
}
