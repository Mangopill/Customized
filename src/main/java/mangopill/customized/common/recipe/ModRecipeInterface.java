package mangopill.customized.common.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public interface ModRecipeInterface<T extends Container> extends Recipe<T> {
    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Nonnull
    default List<ItemStack> getListByWrapper(@Nonnull RecipeWrapper recipeWrapper, int start, int end) {
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
