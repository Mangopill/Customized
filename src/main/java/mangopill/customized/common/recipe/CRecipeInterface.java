package mangopill.customized.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public interface CRecipeInterface<T extends RecipeInput> extends Recipe<T> {
    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    default List<ItemStack> getListByWrapper(RecipeWrapper recipeWrapper, int start, int end) {
        List<ItemStack> ingredient = new ArrayList<>();
        for (int i = start; i < end; ++i) {
            ItemStack itemstack = recipeWrapper.getItem(i);
            if (!itemstack.isEmpty()) {
                ingredient.add(itemstack);
            }
        }
        return ingredient;
    }
}
