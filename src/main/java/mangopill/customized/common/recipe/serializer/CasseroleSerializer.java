package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static mangopill.customized.common.util.RecipeUtil.*;

public class CasseroleSerializer implements RecipeSerializer<CasseroleRecipe> {
    public static final MapCodec<CasseroleRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
            NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).fieldOf("ingredient").forGetter(CasseroleRecipe::getIngredientItem),
            NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).optionalFieldOf("seasoning", NonNullList.of(Ingredient.EMPTY)).forGetter(CasseroleRecipe::getSeasoningItem),
            NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).optionalFieldOf("spice", NonNullList.of(Ingredient.EMPTY)).forGetter(CasseroleRecipe::getSpiceItem),
            Ingredient.CODEC_NONEMPTY.optionalFieldOf("container", Ingredient.EMPTY).forGetter(CasseroleRecipe::getContainerItem),
            FluidIngredient.CODEC_NON_EMPTY.optionalFieldOf("fluid", FluidIngredient.tag(FluidTags.WATER)).forGetter(CasseroleRecipe::getFluidIngredient),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CasseroleRecipe::getOutput),
            Codec.INT.optionalFieldOf("time", 300).forGetter(CasseroleRecipe::getCookingTime),
            Codec.BOOL.optionalFieldOf("heated", true).forGetter(CasseroleRecipe::isHeated)
    ).apply(instance, CasseroleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CasseroleRecipe> STREAM_CODEC = StreamCodec.of(RecipeUtil::toPotNetwork, CasseroleSerializer::fromNetwork);

    @Override
    public MapCodec<CasseroleRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CasseroleRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static CasseroleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        RecipeUtil.PotResult result = getPotResult(buffer);
        return new CasseroleRecipe(result.ingredient(), result.seasoning(), result.spice(), result.container(), result.fluidIngredient(), result.output(), result.cookingTime(), result.isHeated());
    }
}
