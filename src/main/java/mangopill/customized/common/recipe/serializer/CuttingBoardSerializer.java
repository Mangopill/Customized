package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.item.crafting.ProbabilityItemStack;
import mangopill.customized.common.recipe.CuttingBoardRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class CuttingBoardSerializer implements RecipeSerializer<CuttingBoardRecipe> {
    public static final MapCodec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CuttingBoardRecipe::cuttingItem),
                    NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).fieldOf("tool").forGetter(CuttingBoardRecipe::toolItem),
                    NonNullList.codecOf(ItemStack.STRICT_CODEC).optionalFieldOf("result", NonNullList.of(ItemStack.EMPTY)).forGetter(CuttingBoardRecipe::output),
                    NonNullList.codecOf(ProbabilityItemStack.CODEC).optionalFieldOf("additional_result", NonNullList.of(ProbabilityItemStack.EMPTY)).forGetter(CuttingBoardRecipe::probabilityOutput),
                    Codec.INT.optionalFieldOf("times", 1).forGetter(CuttingBoardRecipe::cuttingTimes)
            ).apply(instance, CuttingBoardRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CuttingBoardRecipe> STREAM_CODEC = StreamCodec.of(CuttingBoardSerializer::toNetwork, CuttingBoardSerializer::fromNetwork);

    @Override
    public MapCodec<CuttingBoardRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CuttingBoardRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static CuttingBoardRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int toolLength = buffer.readVarInt();
        NonNullList<Ingredient> tool = NonNullList.withSize(toolLength, Ingredient.EMPTY);
        tool.replaceAll(t -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        int resultLength = buffer.readVarInt();
        NonNullList<ItemStack> result = NonNullList.withSize(resultLength, ItemStack.EMPTY);
        result.replaceAll(r -> ItemStack.STREAM_CODEC.decode(buffer));
        int additionalResultLength = buffer.readVarInt();
        NonNullList<ProbabilityItemStack> additionalResult = NonNullList.withSize(additionalResultLength, ProbabilityItemStack.EMPTY);
        additionalResult.replaceAll(p -> ProbabilityItemStack.STREAM_CODEC.decode(buffer));
        int times = buffer.readVarInt();
        return new CuttingBoardRecipe(ingredient, tool, result, additionalResult, times);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.cuttingItem());
        buffer.writeVarInt(recipe.toolItem().size());
        for (Ingredient tool : recipe.toolItem()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, tool);
        }
        buffer.writeVarInt(recipe.output().size());
        for (ItemStack output : recipe.output()) {
            ItemStack.STREAM_CODEC.encode(buffer, output);
        }
        buffer.writeVarInt(recipe.probabilityOutput().size());
        for (ProbabilityItemStack additionalResult : recipe.probabilityOutput()) {
            ProbabilityItemStack.STREAM_CODEC.encode(buffer, additionalResult);
        }
        buffer.writeVarInt(recipe.cuttingTimes());
    }
}
