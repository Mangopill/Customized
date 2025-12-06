package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

public class CrateRecipe implements CRecipeInterface<RecipeWrapper> {
    private final Ingredient ingredientItem;
    private final int ingredientCount;
    private final ItemStack output;
    private final int cookingTime;
    private final boolean sunny;
    private final int ingredientInput;

    public CrateRecipe(Ingredient ingredientItem, int ingredientCount, ItemStack output, int cookingTime, boolean sunny) {
        this.ingredientItem = ingredientItem;
        this.ingredientCount = ingredientCount;
        this.output = output;
        this.cookingTime = cookingTime;
        this.sunny = sunny;
        ingredientInput = 18;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, ingredientInput);
        int totalCount = 0;
        for (ItemStack stack : ingredient) {
            if (!ingredientItem.test(stack)) {
                return false;
            }
            totalCount += stack.getCount();
        }
        return totalCount >= ingredientCount;
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
        return NonNullList.of(ingredientItem);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.CRATE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.CRATE.get();
    }

    public Ingredient getIngredientItem() {
        return ingredientItem;
    }

    public int getIngredientCount() {
        return ingredientCount;
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

    public boolean isSunny() {
        return sunny;
    }
}
