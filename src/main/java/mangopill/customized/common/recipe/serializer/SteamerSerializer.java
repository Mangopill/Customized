package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.SteamerRecipe;
import mangopill.customized.common.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static mangopill.customized.common.util.RecipeUtil.*;

public class SteamerSerializer implements RecipeSerializer<SteamerRecipe> {
    public static final MapCodec<SteamerRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredient").xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(SteamerRecipe::getIngredientItem),
                    Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("seasoning", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(SteamerRecipe::getSeasoningItem),
                    Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("spice", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(SteamerRecipe::getSpiceItem),
                    Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(SteamerRecipe::getContainerItem),
                    FluidIngredient.CODEC_NON_EMPTY.optionalFieldOf("fluid", FluidIngredient.tag(FluidTags.WATER)).forGetter(SteamerRecipe::getFluidIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SteamerRecipe::getOutput),
                    Codec.INT.optionalFieldOf("time", 300).forGetter(SteamerRecipe::getCookingTime),
                    Codec.BOOL.optionalFieldOf("heated", true).forGetter(SteamerRecipe::isHeated)
            ).apply(instance, SteamerRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SteamerRecipe> STREAM_CODEC = StreamCodec.of(RecipeUtil::toPotNetwork, SteamerSerializer::fromNetwork);

    @Override
    public MapCodec<SteamerRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SteamerRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static SteamerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        PotResult result = getPotResult(buffer);
        return new SteamerRecipe(result.ingredient(), result.seasoning(), result.spice(), result.container(), result.fluidIngredient(), result.output(), result.cookingTime(), result.isHeated());
    }
}