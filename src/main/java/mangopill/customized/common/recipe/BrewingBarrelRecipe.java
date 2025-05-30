package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.registry.ModRecipeSerializerRegistry;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class BrewingBarrelRecipe implements ModRecipeInterface<RecipeWrapper>{
    private final NonNullList<Ingredient> ingredientItem;
    private final Ingredient containerItem;
    private final ItemStack output;
    private final int cookingTime;
    private final int ingredientInput;
    private final ResourceLocation id;

    public BrewingBarrelRecipe(NonNullList<Ingredient> ingredientItem, Ingredient containerItem, ItemStack output, int cookingTime, ResourceLocation id) {
        this.ingredientItem = ingredientItem;
        this.containerItem = containerItem;
        this.output = output;
        this.cookingTime = cookingTime;
        ingredientInput = 4;
        this.id = id;
    }

    @Override
    public boolean matches(@Nonnull RecipeWrapper recipeWrapper, @Nonnull Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        return ingredient.size() == ingredientItem.size() && RecipeMatcher.findMatches(ingredient, ingredientItem) != null;
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
        return ingredientItem;
    }

    @Override
    public @Nonnull ResourceLocation getId() {
        return id;
    }

    @Override
    public @Nonnull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializerRegistry.BREWING_BARREL.get();
    }

    @Override
    public @Nonnull RecipeType<?> getType() {
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
