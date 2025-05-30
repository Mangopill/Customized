package mangopill.customized.common.recipe.serializer;

import com.google.gson.*;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
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

public class BrewingBarrelSerializer implements RecipeSerializer<BrewingBarrelRecipe> {
    @Override
    public @Nonnull BrewingBarrelRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject jsonObject) {
        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(jsonObject, "ingredient");
        NonNullList<Ingredient> ingredient = JsonUtil.jsonArrayToNonNullList(ingredientsArray);
        JsonElement containerIn = GsonHelper.getAsJsonObject(jsonObject, "container");
        Ingredient container = Ingredient.fromJson(containerIn);
        final ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true);
        final int cookingTime = GsonHelper.getAsInt(jsonObject, "time", 200);
        return new BrewingBarrelRecipe(ingredient, container, output, cookingTime, id);
    }

    @Nullable
    @Override
    public BrewingBarrelRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.fromNetwork(buffer));
        Ingredient container = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int cookingTime = buffer.readVarInt();
        return new BrewingBarrelRecipe(ingredient, container, output, cookingTime, id);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BrewingBarrelRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            ingredient.toNetwork(buffer);
        }
        recipe.getContainerItem().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
