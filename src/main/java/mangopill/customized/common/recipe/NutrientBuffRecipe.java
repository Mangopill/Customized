package mangopill.customized.common.recipe;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.registry.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;

public record NutrientBuffRecipe(Holder<MobEffect> effect, List<HashSet<Pair<String, Float>>> nutrientCategory, NonNullList<Ingredient> pot, float duration, float probability, float shrinkNutrition, float shrinkSaturation) implements CRecipeInterface<RecipeInput> {

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
    public NonNullList<Ingredient> getIngredients() {
        return pot;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.NUTRIENT_BUFF.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.NUTRIENT_BUFF.get();
    }
}
