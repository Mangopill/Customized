package mangopill.customized.common.recipe.serializer;

import com.google.gson.*;
import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.util.JsonUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CasseroleSerializer implements RecipeSerializer<CasseroleRecipe> {

    @Override
    public @Nonnull CasseroleRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject jsonObject) {
        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(jsonObject, "ingredient");
        NonNullList<Ingredient> ingredient = JsonUtil.jsonArrayToNonNullList(ingredientsArray);
        JsonArray seasoningArray = GsonHelper.getAsJsonArray(jsonObject, "seasoning");
        NonNullList<Ingredient> seasoning = JsonUtil.jsonArrayToNonNullList(seasoningArray);
        JsonElement spiceIn = GsonHelper.getAsJsonObject(jsonObject, "spice");
        Ingredient spice = Ingredient.fromJson(spiceIn);
        final ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true);
        final int cookingTime = GsonHelper.getAsInt(jsonObject, "time", 300);
        return new CasseroleRecipe(ingredient, seasoning, spice, output, cookingTime, id);
    }

    @Nullable
    @Override
    public CasseroleRecipe fromNetwork(@Nonnull ResourceLocation id, FriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.fromNetwork(buffer));
        int seasoningLength = buffer.readVarInt();
        NonNullList<Ingredient> seasoning = NonNullList.withSize(seasoningLength, Ingredient.EMPTY);
        seasoning.replaceAll(j -> Ingredient.fromNetwork(buffer));
        Ingredient spice = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int cookingTime = buffer.readVarInt();
        return new CasseroleRecipe(ingredient, seasoning, spice, output, cookingTime, id);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CasseroleRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            ingredient.toNetwork(buffer);
        }
        buffer.writeVarInt(recipe.getSeasoningItem().size());
        for (Ingredient ingredient : recipe.getSeasoningItem()) {
            ingredient.toNetwork(buffer);
        }
        recipe.getSpiceItem().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
