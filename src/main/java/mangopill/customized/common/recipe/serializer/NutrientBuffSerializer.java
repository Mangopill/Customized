package mangopill.customized.common.recipe.serializer;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.NutrientBuffRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.*;

public class NutrientBuffSerializer implements RecipeSerializer<NutrientBuffRecipe> {
    public static final MapCodec<NutrientBuffRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MobEffect.CODEC.fieldOf("effect").forGetter(NutrientBuffRecipe::effect),
                    Codec.compoundList(Codec.STRING, Codec.FLOAT).xmap(HashSet::new, ArrayList::new).listOf().fieldOf("nutrient_category").forGetter(NutrientBuffRecipe::nutrientCategory),
                    Ingredient.LIST_CODEC_NONEMPTY.optionalFieldOf("pot", NonNullList.of(Ingredient.EMPTY)).xmap(NonNullList::copyOf, nonNullList -> nonNullList).forGetter(NutrientBuffRecipe::pot),
                    Codec.FLOAT.optionalFieldOf("duration", 1.0F).forGetter(NutrientBuffRecipe::duration),
                    Codec.FLOAT.optionalFieldOf("probability", 0.01F).forGetter(NutrientBuffRecipe::probability),
                    Codec.FLOAT.optionalFieldOf("shrink_nutrition", 0.0F).forGetter(NutrientBuffRecipe::shrinkNutrition),
                    Codec.FLOAT.optionalFieldOf("shrink_saturation", 0.0F).forGetter(NutrientBuffRecipe::shrinkSaturation)
            ).apply(instance, NutrientBuffRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, NutrientBuffRecipe> STREAM_CODEC = StreamCodec.of(NutrientBuffSerializer::toNetwork, NutrientBuffSerializer::fromNetwork);

    @Override
    public MapCodec<NutrientBuffRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, NutrientBuffRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static NutrientBuffRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Holder<MobEffect> effect = MobEffect.STREAM_CODEC.decode(buffer);
        int outerListSize = buffer.readVarInt();
        List<HashSet<Pair<String, Float>>> outerList = new ArrayList<>(outerListSize);
        for (int i = 0; i < outerListSize; i++) {
            int innerListSize = buffer.readVarInt();
            HashSet<Pair<String, Float>> innerList = new HashSet<>(innerListSize);
            for (int j = 0; j < innerListSize; j++) {
                String key = buffer.readUtf();
                Float value = buffer.readFloat();
                innerList.add(Pair.of(key, value));
            }
            outerList.add(innerList);
        }
        int potLength = buffer.readVarInt();
        NonNullList<Ingredient> pot = NonNullList.withSize(potLength, Ingredient.EMPTY);
        pot.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        float duration = buffer.readFloat();
        float probability = buffer.readFloat();
        float shrinkNutrition = buffer.readFloat();
        float shrinkSaturation = buffer.readFloat();
        return new NutrientBuffRecipe(effect, outerList, pot, duration, probability, shrinkNutrition, shrinkSaturation);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, NutrientBuffRecipe recipe) {
        MobEffect.STREAM_CODEC.encode(buffer, recipe.effect());
        List<HashSet<Pair<String, Float>>> outerList = recipe.nutrientCategory();
        buffer.writeVarInt(outerList.size());
        for (HashSet<Pair<String, Float>> innerList : outerList) {
            buffer.writeVarInt(innerList.size());
            for (Pair<String, Float> pair : innerList) {
                buffer.writeUtf(pair.getFirst());
                buffer.writeFloat(pair.getSecond());
            }
        }
        buffer.writeVarInt(recipe.pot().size());
        for (Ingredient ingredient : recipe.pot()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        buffer.writeFloat(recipe.duration());
        buffer.writeFloat(recipe.probability());
        buffer.writeFloat(recipe.shrinkNutrition());
        buffer.writeFloat(recipe.shrinkSaturation());
    }
}