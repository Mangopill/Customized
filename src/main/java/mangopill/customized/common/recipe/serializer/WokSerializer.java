package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.WokRecipe;
import mangopill.customized.common.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static mangopill.customized.common.util.RecipeUtil.*;

public class WokSerializer implements RecipeSerializer<WokRecipe> {
    public static final MapCodec<WokRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredient").xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(WokRecipe::getIngredientItem),
                    Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("seasoning", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(WokRecipe::getSeasoningItem),
                    Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("spice", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(WokRecipe::getSpiceItem),
                    Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(WokRecipe::getContainerItem),
                    FluidIngredient.CODEC_NON_EMPTY.optionalFieldOf("fluid", FluidIngredient.tag(FluidTags.WATER)).forGetter(WokRecipe::getFluidIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WokRecipe::getOutput),
                    Codec.INT.optionalFieldOf("time", 300).forGetter(WokRecipe::getCookingTime),
                    Codec.BOOL.optionalFieldOf("heated", true).forGetter(WokRecipe::isHeated)
            ).apply(instance, WokRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WokRecipe> STREAM_CODEC = StreamCodec.of(RecipeUtil::toPotNetwork, WokSerializer::fromNetwork);

    @Override
    public MapCodec<WokRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WokRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static WokRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        PotResult result = getPotResult(buffer);
        return new WokRecipe(result.ingredient(), result.seasoning(), result.spice(), result.container(), result.fluidIngredient(), result.output(), result.cookingTime(), result.isHeated());
    }
}