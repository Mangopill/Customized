package mangopill.customized.common.util.value;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.IntStream;

public class PropertyValue {
    private static final NutrientCategory[] CATEGORY = NutrientCategory.values();
    private final float[] value = new float[CATEGORY.length];
    private int size;

    public static PropertyValue fromJson(JsonElement json) {
        final PropertyValue propertyValue = new PropertyValue();
        JsonObject object = json.getAsJsonObject();
        object.entrySet().forEach(entry -> {
            String category = entry.getKey().toUpperCase(Locale.ROOT);
            propertyValue.put(NutrientCategory.valueOf(category), entry.getValue().getAsFloat());
        });
        return propertyValue;
    }

    public JsonElement toJson() {
        final JsonObject object = new JsonObject();
        this.toSet().forEach(entry -> object.addProperty(entry.getKey().name(), entry.getValue()));
        return object;
    }

    public boolean has(@Nonnull NutrientCategory category) {
        return value[category.ordinal()] > 0.0F;
    }
    public boolean notHas(@Nonnull NutrientCategory category) {
        return !this.has(category);
    }

    public void put(@Nonnull NutrientCategory category, float value) {
        if (Float.isNaN(value) || value <= 0.0F) {
            this.remove(category);
            return;
        }
        if (notHas(category)) {
            size++;
        }
        this.value[category.ordinal()] = value;
    }

    public void remove(@Nonnull NutrientCategory category) {
        if (notHas(category)) {
            return;
        }
        value[category.ordinal()] = 0.0F;
        size--;
    }

    public Set<Pair<NutrientCategory, Float>> toSet() {
        ImmutableSet.Builder<Pair<NutrientCategory, Float>> builder = ImmutableSet.builder();
        for (NutrientCategory category : CATEGORY){
            if (value[category.ordinal()] > 0.0F) {
            builder.add(Pair.of(category, value[category.ordinal()]));
            }
        }
        return builder.build();
    }

    public static PropertyValue fromNetwork(FriendlyByteBuf buffer) {
        PropertyValue propertyValue = new PropertyValue();
        int length = buffer.readByte();
        IntStream.range(0, length).forEach(i -> {
            NutrientCategory category = buffer.readEnum(NutrientCategory.class);
            float value = buffer.readFloat();
            propertyValue.put(category, value);
        });
        return propertyValue;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        Set<Pair<NutrientCategory, Float>> entrySet = this.toSet();
        buffer.writeByte(entrySet.size());
        entrySet.forEach(entry -> {
            buffer.writeEnum(entry.getKey());
            buffer.writeFloat(entry.getValue());
        });
    }

    public boolean isEmpty() {
        return size <= 0;
    }

    public void replace() {
        Arrays.fill(value, 0.0F);
        size = 0;
    }

    public float getBigger(@Nonnull NutrientCategory category) {
        return Math.max(value[category.ordinal()], 0.0F);
    }

    public static NutrientCategory[] getCategory() {
        return CATEGORY;
    }

    public float[] getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }
}
