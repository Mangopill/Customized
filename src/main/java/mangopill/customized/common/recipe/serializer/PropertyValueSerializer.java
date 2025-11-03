package mangopill.customized.common.recipe.serializer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class PropertyValueSerializer implements RecipeSerializer<PropertyValueRecipe> {
    public static final MapCodec<PropertyValueRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    NeoForgeExtraCodecs.xor(
                            NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC).fieldOf("item"),
                            NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC).fieldOf("tag")
                    ).forGetter(recipe -> recipe.item() ? Either.left(recipe.name()) : Either.right(recipe.name())),
                    PropertyValue.CODEC.fieldOf("value").forGetter(PropertyValueRecipe::propertyValue)
            ).apply(instance, (itemOrTag, propertyValue) -> {
                boolean isItem = itemOrTag.left().isPresent();
                Set<ResourceLocation> name = isItem ? itemOrTag.left().get() : itemOrTag.right().get();
                return new PropertyValueRecipe(name, propertyValue, isItem);
            })
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PropertyValueRecipe> STREAM_CODEC = StreamCodec.of(
            PropertyValueSerializer::toNetwork, PropertyValueSerializer::fromNetwork
    );

    private static PropertyValueRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        boolean isItem = buffer.readBoolean();
        HashSet<ResourceLocation> name = new HashSet<>();
        int length = buffer.readVarInt();
        IntStream.range(0, length).forEach(i -> name.add(buffer.readResourceLocation()));
        PropertyValue propertyValue = PropertyValue.STREAM_CODEC.decode(buffer);
        return new PropertyValueRecipe(name, propertyValue, isItem);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, PropertyValueRecipe recipe) {
        buffer.writeBoolean(recipe.item());
        buffer.writeVarInt(recipe.name().size());
        recipe.name().forEach(buffer::writeResourceLocation);
        PropertyValue.STREAM_CODEC.encode(buffer, recipe.propertyValue());
    }

    @Override
    public MapCodec<PropertyValueRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PropertyValueRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
