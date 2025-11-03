package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public record CuttingBoardRecipe(Ingredient cuttingItem, NonNullList<Ingredient> toolItem,
                                 NonNullList<ItemStack> output, NonNullList<ItemStack> probabilityOutput,
                                 float probability, int cuttingTimes) implements CRecipeInterface<RecipeWrapper> {
    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return this.cuttingItem.test(recipeWrapper.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return this.output.getFirst().copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.getFirst();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> combinedIngredient = NonNullList.create();
        combinedIngredient.add(cuttingItem);
        combinedIngredient.addAll(toolItem);
        return combinedIngredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.CUTTING_BOARD.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.CUTTING_BOARD.get();
    }
}
