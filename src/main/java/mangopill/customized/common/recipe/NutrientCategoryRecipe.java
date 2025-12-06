package mangopill.customized.common.recipe;

import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record NutrientCategoryRecipe(ResourceLocation icon, String name, String color, float nutrition, float saturation) implements CRecipeInterface<RecipeInput> {

    public TextColor getColor() {
        return TextColor.fromRgb(Integer.decode(color));
    }

    public int getColorWithAlpha() {
        int rgb = Integer.decode(color);
        int alpha = 0xCC;
        return (alpha << 24) | (rgb & 0x00FFFFFF);
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
