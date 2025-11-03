package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.CrateRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrateSerializer implements RecipeSerializer<CrateRecipe> {
    private static final MapCodec<CrateRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredient").xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(CrateRecipe::getIngredientItem),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CrateRecipe::getOutput),
                    Codec.INT.optionalFieldOf("time", 200).forGetter(CrateRecipe::getCookingTime)
            ).apply(instance, CrateRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrateRecipe> STREAM_CODEC = StreamCodec.of(CrateSerializer::toNetwork, CrateSerializer::fromNetwork);

    @Override
    public MapCodec<CrateRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CrateRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static CrateRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int cookingTime = buffer.readVarInt();
        return new CrateRecipe(ingredient, output, cookingTime);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CrateRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
