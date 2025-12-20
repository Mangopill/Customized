package mangopill.customized.common.util;

import mangopill.customized.common.recipe.AbstractPotRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class RecipeUtil {
    private RecipeUtil() {
    }

    public static Ingredient mergeIngredients(Collection<Ingredient> ingredients) {
        if (ingredients.isEmpty()) return Ingredient.EMPTY;
        return mergeIngredients(ingredients.stream());
    }

    public static Ingredient mergeIngredients(Stream<Ingredient> ingredients) {
        return Ingredient.of(ingredients.flatMap(ingredient -> Arrays.stream(ingredient.getItems())));
    }

    public static Ingredient toIngredient(Collection<ItemStack> itemStacks) {
        if (itemStacks.isEmpty()) return Ingredient.EMPTY;
        return Ingredient.of(itemStacks.stream());
    }

    public static Ingredient toIngredient(Collection<ItemStack> itemStacks, Consumer<ItemStack> consumer) {
        if (itemStacks.isEmpty()) return Ingredient.EMPTY;
        return Ingredient.of(itemStacks.stream().peek(consumer));
    }

    public static List<ItemStack> toStackList(Collection<Ingredient> ingredients) {
        if (ingredients.isEmpty()) return new ArrayList<>();
        return ingredients.stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems())).toList();
    }

    public static List<ItemStack> toStackList(Collection<Ingredient> ingredients, Consumer<ItemStack> consumer) {
        return toStackList(ingredients).stream().map(ItemStack::copy).peek(consumer).toList();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> Optional<T> getCheckRecipeOptionalFor(RecipeManager.CachedCheck<I, T> check, I recipeWrapper, @Nullable Level level) {
        return level != null ? check.getRecipeFor(recipeWrapper, level).map(RecipeHolder::value) : Optional.empty();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<T> getRecipeListFor(RecipeType<T> recipeType, I input, @Nullable Level level) {
        return level != null ? level.getRecipeManager().getRecipesFor(recipeType, input, level).stream().map(RecipeHolder::value).toList() : new ArrayList<>();
    }

    public static<I extends RecipeInput, T extends Recipe<I>> List<T> getAllRecipeList(RecipeType<T> recipeType, @Nullable Level level) {
        return level != null ? level.getRecipeManager().getAllRecipesFor(recipeType).stream().map(RecipeHolder::value).toList() : new ArrayList<>();
    }

    public static void toPotNetwork(RegistryFriendlyByteBuf buffer, AbstractPotRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        buffer.writeVarInt(recipe.getSeasoningItem().size());
        for (Ingredient seasoning : recipe.getSeasoningItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, seasoning);
        }
        buffer.writeVarInt(recipe.getSpiceItem().size());
        for (Ingredient spice : recipe.getSpiceItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, spice);
        }
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getContainerItem());
        FluidIngredient.STREAM_CODEC.encode(buffer, recipe.getFluidIngredient());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
        buffer.writeBoolean(recipe.isHeated());
    }

    public static PotResult getPotResult(RegistryFriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        int seasoningLength = buffer.readVarInt();
        NonNullList<Ingredient> seasoning = NonNullList.withSize(seasoningLength, Ingredient.EMPTY);
        seasoning.replaceAll(j -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        int spiceLength = buffer.readVarInt();
        NonNullList<Ingredient> spice = NonNullList.withSize(spiceLength, Ingredient.EMPTY);
        spice.replaceAll(j -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        Ingredient container = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        FluidIngredient fluidIngredient = FluidIngredient.STREAM_CODEC.decode(buffer);
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int cookingTime = buffer.readVarInt();
        boolean heated = buffer.readBoolean();
        return new PotResult(ingredient, seasoning, spice, container, fluidIngredient, output, cookingTime, heated);
    }

    public record PotResult(NonNullList<Ingredient> ingredient, NonNullList<Ingredient> seasoning, NonNullList<Ingredient> spice, Ingredient container, FluidIngredient fluidIngredient, ItemStack output, int cookingTime, boolean isHeated) {}
}
