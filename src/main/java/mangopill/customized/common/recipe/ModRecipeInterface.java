package mangopill.customized.common.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface ModRecipeInterface<T extends RecipeInput> extends Recipe<T> {
    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    default boolean isSpecial() {
        return true;
    }
}
