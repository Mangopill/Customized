package mangopill.customized.common.util;

import mangopill.customized.common.recipe.AbstractPotRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public final class RecipeUtil {
    private RecipeUtil() {
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
