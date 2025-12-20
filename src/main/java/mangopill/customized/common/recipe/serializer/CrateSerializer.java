package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.CrateRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrateSerializer implements RecipeSerializer<CrateRecipe> {
    public static final MapCodec<CrateRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CrateRecipe::ingredientItem),
                    Codec.INT.fieldOf("count").forGetter(CrateRecipe::ingredientCount),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CrateRecipe::output),
                    Codec.INT.optionalFieldOf("time", 200).forGetter(CrateRecipe::cookingTime),
                    Codec.BOOL.optionalFieldOf("sunny", true).forGetter(CrateRecipe::sunny)
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
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int ingredientCount = buffer.readVarInt();
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int cookingTime = buffer.readVarInt();
        boolean sunny = buffer.readBoolean();
        return new CrateRecipe(ingredient, ingredientCount, output, cookingTime, sunny);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CrateRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredientItem());
        buffer.writeVarInt(recipe.ingredientCount());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output());
        buffer.writeVarInt(recipe.cookingTime());
        buffer.writeBoolean(recipe.sunny());
    }
}
