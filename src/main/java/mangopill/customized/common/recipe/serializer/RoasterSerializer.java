package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.RoasterRecipe;
import mangopill.customized.common.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static mangopill.customized.common.util.RecipeUtil.*;

public class RoasterSerializer implements RecipeSerializer<RoasterRecipe> {
    public static final MapCodec<RoasterRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).fieldOf("ingredient").forGetter(RoasterRecipe::getIngredientItem),
                    NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).optionalFieldOf("seasoning", NonNullList.of(Ingredient.EMPTY)).forGetter(RoasterRecipe::getSeasoningItem),
                    NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).optionalFieldOf("spice", NonNullList.of(Ingredient.EMPTY)).forGetter(RoasterRecipe::getSpiceItem),
                    Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(RoasterRecipe::getContainerItem),
                    FluidIngredient.CODEC_NON_EMPTY.optionalFieldOf("fluid", FluidIngredient.tag(FluidTags.WATER)).forGetter(RoasterRecipe::getFluidIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RoasterRecipe::getOutput),
                    Codec.INT.optionalFieldOf("time", 300).forGetter(RoasterRecipe::getCookingTime),
                    Codec.BOOL.optionalFieldOf("heated", true).forGetter(RoasterRecipe::isHeated)
            ).apply(instance, RoasterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> STREAM_CODEC = StreamCodec.of(RecipeUtil::toPotNetwork, RoasterSerializer::fromNetwork);

    @Override
    public MapCodec<RoasterRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static RoasterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        PotResult result = getPotResult(buffer);
        return new RoasterRecipe(result.ingredient(), result.seasoning(), result.spice(), result.container(), result.fluidIngredient(), result.output(), result.cookingTime(), result.isHeated());
    }
}