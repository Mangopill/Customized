package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.*;
import net.minecraft.core.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

public record CrateRecipe(Ingredient ingredientItem, int ingredientCount, ItemStack output, int cookingTime, boolean sunny) implements CRecipeInterface<RecipeWrapper> {
    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        List<ItemStack> ingredient = getListByWrapper(recipeWrapper, 0, 18);
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
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
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
}
