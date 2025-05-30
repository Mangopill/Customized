package mangopill.customized.common.recipe;

import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
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
    private final ResourceLocation id;

    public AbstractPotRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem, Ingredient spiceItem, ItemStack output, int cookingTime, RecipeSerializer<?> recipeSerializer, RecipeType<?> recipeType, int ingredientSlot, int seasoningSlot, ResourceLocation id) {
        this.ingredientItem = ingredientItem;
        this.seasoningItem = seasoningItem;
        this.spiceItem = spiceItem;
        this.output = output;
        this.cookingTime = cookingTime;
        this.recipeSerializer = recipeSerializer;
        this.recipeType = recipeType;
        ingredientInput = ingredientSlot;
        seasoningInput = seasoningSlot;
        this.id = id;
    }

    @Override
    public boolean matches(@Nonnull RecipeWrapper recipeWrapper, @Nonnull Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        List<ItemStack> seasoning = getListByWrapper(recipeWrapper, ingredientInput, ingredientInput + seasoningInput);
        return ItemStack.isSameItem(spiceItem.getItems()[0], recipeWrapper.getItem(ingredientInput + seasoningInput))
                && ingredient.size() == ingredientItem.size()
                && seasoning.size() == seasoningItem.size()
                && RecipeMatcher.findMatches(ingredient, ingredientItem) != null
                && RecipeMatcher.findMatches(seasoning, seasoningItem) != null;
    }

    @Override
    public @Nonnull ItemStack assemble(@Nonnull RecipeWrapper recipeWrapper, @Nonnull RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public @Nonnull ItemStack getResultItem(@Nonnull RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public @Nonnull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> combinedIngredient = NonNullList.create();
        combinedIngredient.addAll(ingredientItem);
        combinedIngredient.addAll(seasoningItem);
        if (spiceItem != null && !spiceItem.isEmpty()) {
            combinedIngredient.add(spiceItem);
        }
        return combinedIngredient;
    }

    @Override
    public @Nonnull RecipeSerializer<?> getSerializer() {
        return recipeSerializer;
    }

    @Override
    public @Nonnull RecipeType<?> getType() {
        return recipeType;
    }

    @Override
    public @Nonnull ResourceLocation getId() {
        return id;
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
