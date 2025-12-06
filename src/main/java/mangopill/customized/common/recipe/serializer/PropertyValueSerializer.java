package mangopill.customized.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.*;

public class PropertyValueSerializer implements RecipeSerializer<PropertyValueRecipe> {
    public static final MapCodec<PropertyValueRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PropertyValueGroup.CODEC.listOf().fieldOf("group").forGetter(PropertyValueRecipe::groups)
            ).apply(instance, PropertyValueRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PropertyValueRecipe> STREAM_CODEC = StreamCodec.of(
            PropertyValueSerializer::toNetwork, PropertyValueSerializer::fromNetwork
    );

    public record PropertyValueGroup(HashSet<ResourceLocation> items, HashSet<ResourceLocation> tags, PropertyValue propertyValue) {
        public static final Codec<PropertyValueGroup> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ResourceLocation.CODEC.listOf().optionalFieldOf("item", List.of())
                                .xmap(HashSet::new, ArrayList::new)
                                .forGetter(PropertyValueGroup::items),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("tag", List.of())
                                .xmap(HashSet::new, ArrayList::new)
                                .forGetter(PropertyValueGroup::tags),
                        PropertyValue.CODEC.fieldOf("value").forGetter(PropertyValueGroup::propertyValue)
                ).apply(instance, PropertyValueGroup::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, PropertyValueGroup> STREAM_CODEC = StreamCodec.of(
                PropertyValueGroup::toNetwork, PropertyValueGroup::fromNetwork
        );

        private static PropertyValueGroup fromNetwork(RegistryFriendlyByteBuf buffer) {
            int itemCount = buffer.readVarInt();
            HashSet<ResourceLocation> items = new HashSet<>();
            for (int i = 0; i < itemCount; i++) {
                items.add(buffer.readResourceLocation());
            }
            int tagCount = buffer.readVarInt();
            HashSet<ResourceLocation> tags = new HashSet<>();
            for (int i = 0; i < tagCount; i++) {
                tags.add(buffer.readResourceLocation());
            }
            PropertyValue propertyValue = PropertyValue.STREAM_CODEC.decode(buffer);
            return new PropertyValueGroup(items, tags, propertyValue);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, PropertyValueGroup group) {
            buffer.writeVarInt(group.items().size());
            for (ResourceLocation item : group.items()) {
                buffer.writeResourceLocation(item);
            }
            buffer.writeVarInt(group.tags().size());
            for (ResourceLocation tag : group.tags()) {
                buffer.writeResourceLocation(tag);
            }
            PropertyValue.STREAM_CODEC.encode(buffer, group.propertyValue());
        }
    }

    private static PropertyValueRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        int groupCount = buffer.readVarInt();
        List<PropertyValueGroup> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groups.add(PropertyValueGroup.STREAM_CODEC.decode(buffer));
        }
        return new PropertyValueRecipe(groups);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, PropertyValueRecipe recipe) {
        buffer.writeVarInt(recipe.groups().size());
        for (PropertyValueGroup group : recipe.groups()) {
            PropertyValueGroup.STREAM_CODEC.encode(buffer, group);
        }
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
