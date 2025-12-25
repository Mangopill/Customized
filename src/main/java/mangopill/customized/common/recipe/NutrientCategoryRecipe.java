package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import static mangopill.customized.common.util.CStringUtil.*;

public record NutrientCategoryRecipe(ResourceLocation icon, String name, String color, float nutrition, float saturation) implements CRecipeInterface<RecipeInput> {

    public TextColor getColor() {
        return getColorFromString(color);
    }

    public int getColorWithAlpha() {
        return getColorWithAlphaFromString(color);
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.NUTRIENT_CATEGORY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.NUTRIENT_CATEGORY.get();
    }
}
