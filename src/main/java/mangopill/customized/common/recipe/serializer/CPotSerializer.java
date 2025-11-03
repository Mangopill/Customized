package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.CasseroleRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CPotSerializer implements RecipeSerializer<CasseroleRecipe> {
    private static final MapCodec<CasseroleRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
            Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredient").xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(CasseroleRecipe::getIngredientItem),
            Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("seasoning", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(CasseroleRecipe::getSeasoningItem),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("spice", Ingredient.EMPTY).forGetter(CasseroleRecipe::getSpiceItem),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(CasseroleRecipe::getContainerItem),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CasseroleRecipe::getOutput),
            Codec.INT.optionalFieldOf("time", 300).forGetter(CasseroleRecipe::getCookingTime)
    ).apply(instance, CasseroleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CasseroleRecipe> STREAM_CODEC = StreamCodec.of(CPotSerializer::toNetwork, CPotSerializer::fromNetwork);

    @Override
    public MapCodec<CasseroleRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CasseroleRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static CasseroleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ingredientLength = buffer.readVarInt();
        NonNullList<Ingredient> ingredient = NonNullList.withSize(ingredientLength, Ingredient.EMPTY);
        ingredient.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        int seasoningLength = buffer.readVarInt();
        NonNullList<Ingredient> seasoning = NonNullList.withSize(seasoningLength, Ingredient.EMPTY);
        seasoning.replaceAll(j -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        Ingredient spice = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient container = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int cookingTime = buffer.readVarInt();
        return new CasseroleRecipe(ingredient, seasoning, spice, container, output, cookingTime);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CasseroleRecipe recipe) {
        buffer.writeVarInt(recipe.getIngredientItem().size());
        for (Ingredient ingredient : recipe.getIngredientItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        buffer.writeVarInt(recipe.getSeasoningItem().size());
        for (Ingredient seasoning : recipe.getSeasoningItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, seasoning);
        }
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getSpiceItem());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getContainerItem());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}
