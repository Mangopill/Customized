package mangopill.customized.common.recipe.serializer;

import com.google.gson.*;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class PropertyValueSerializer implements RecipeSerializer<PropertyValueRecipe> {

    @Override
    public @Nonnull PropertyValueRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject jsonObject) {
        Set<ResourceLocation> name = new HashSet<>();
        PropertyValue propertyValue = PropertyValue.fromJson(GsonHelper.getAsJsonObject(jsonObject, "value"));
        boolean isItem = jsonObject.has("item");
        GsonHelper.getAsJsonArray(jsonObject, isItem ? "item" : "tag").forEach(n -> name.add(new ResourceLocation(GsonHelper.convertToString(n, isItem ? "item" : "tag"))));
        return new PropertyValueRecipe(name, propertyValue, isItem, id);
    }

    public PropertyValueRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        boolean isItem = buffer.readBoolean();
        HashSet<ResourceLocation> name = new HashSet<>();
        int length = buffer.readVarInt();
        IntStream.range(0, length).forEach(i -> name.add(buffer.readResourceLocation()));
        PropertyValue propertyValue = PropertyValue.fromNetwork(buffer);
        return new PropertyValueRecipe(name, propertyValue, isItem, id);
    }

    public void toNetwork(FriendlyByteBuf buffer, PropertyValueRecipe recipe) {
        buffer.writeBoolean(recipe.item());
        buffer.writeVarInt(recipe.name().size());
        recipe.name().forEach(buffer::writeResourceLocation);
        recipe.propertyValue().toNetwork(buffer);
    }
}
