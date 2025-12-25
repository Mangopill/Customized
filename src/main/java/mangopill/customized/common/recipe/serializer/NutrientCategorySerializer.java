package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import static mangopill.customized.common.util.CStringUtil.*;

public class NutrientCategorySerializer implements RecipeSerializer<NutrientCategoryRecipe> {
    public static final MapCodec<NutrientCategoryRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("icon", getCPngLoc("textures/item/chef_hat")).forGetter(NutrientCategoryRecipe::icon),
                    Codec.STRING.fieldOf("name").forGetter(NutrientCategoryRecipe::name),
                    Codec.STRING.optionalFieldOf("color", "0xFFFFFF").forGetter(NutrientCategoryRecipe::color),
                    Codec.FLOAT.optionalFieldOf("nutrition", 0.0F).forGetter(NutrientCategoryRecipe::nutrition),
                    Codec.FLOAT.optionalFieldOf("saturation", 0.0F).forGetter(NutrientCategoryRecipe::saturation)
            ).apply(instance, NutrientCategoryRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, NutrientCategoryRecipe> STREAM_CODEC = StreamCodec.of(NutrientCategorySerializer::toNetwork, NutrientCategorySerializer::fromNetwork);

    @Override
    public MapCodec<NutrientCategoryRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, NutrientCategoryRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    private static NutrientCategoryRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        ResourceLocation icon = buffer.readResourceLocation();
        String name = buffer.readUtf();
        String color = buffer.readUtf();
        float nutrition = buffer.readFloat();
        float saturation = buffer.readFloat();
        return new NutrientCategoryRecipe(icon, name, color, nutrition, saturation);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, NutrientCategoryRecipe recipe) {
        buffer.writeResourceLocation(recipe.icon());
        buffer.writeUtf(recipe.name());
        buffer.writeUtf(recipe.color());
        buffer.writeFloat(recipe.nutrition());
        buffer.writeFloat(recipe.saturation());
    }
}