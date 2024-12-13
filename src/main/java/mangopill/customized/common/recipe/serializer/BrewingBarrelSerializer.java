package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class BrewingBarrelSerializer implements RecipeSerializer<BrewingBarrelRecipe> {
    private static final MapCodec<BrewingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredient").xmap(ingredient -> {
                        NonNullList<Ingredient> ingredientList = NonNullList.create();
                        ingredientList.addAll(ingredient);
                        return ingredientList;
                    }, ingredient -> ingredient).forGetter(BrewingBarrelRecipe::getIngredientItem),
                    Ingredient.CODEC_NONEMPTY.fieldOf("container").forGetter(BrewingBarrelRecipe::getContainerItem),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BrewingBarrelRecipe::getOutput),
                    Codec.INT.optionalFieldOf("time", 200).forGetter(BrewingBarrelRecipe::getCookingTime)
            ).apply(instance, BrewingBarrelRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingBarrelRecipe> STREAM_CODEC = StreamCodec.of(BrewingBarrelSerializer::toNetwork, BrewingBarrelSerializer::fromNetwork);

    @Override
    public @NotNull MapCodec<BrewingBarrelRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, BrewingBarrelRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static BrewingBarrelRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        Ingredient container = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int cookingTime = buffer.readVarInt();
        return new BrewingBarrelRecipe(ingredient, container, output, cookingTime);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, BrewingBarrelRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getContainerItem());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
