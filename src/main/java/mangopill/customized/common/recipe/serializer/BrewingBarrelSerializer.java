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

public class BrewingBarrelSerializer implements RecipeSerializer<BrewingBarrelRecipe> {
    public static final MapCodec<BrewingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).fieldOf("ingredient").forGetter(BrewingBarrelRecipe::ingredientItem),
                    Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(BrewingBarrelRecipe::containerItem),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BrewingBarrelRecipe::output),
                    Codec.INT.optionalFieldOf("time", 200).forGetter(BrewingBarrelRecipe::cookingTime)
            ).apply(instance, BrewingBarrelRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingBarrelRecipe> STREAM_CODEC = StreamCodec.of(BrewingBarrelSerializer::toNetwork, BrewingBarrelSerializer::fromNetwork);

    @Override
    public MapCodec<BrewingBarrelRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BrewingBarrelRecipe> streamCodec() {
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
        buffer.writeVarInt(recipe.ingredientItem().size());
        for (Ingredient ingredient : recipe.ingredientItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.containerItem());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output());
        buffer.writeVarInt(recipe.cookingTime());
    }
}
