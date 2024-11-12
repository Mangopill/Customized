package mangopill.customized.common.util.value;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import mangopill.customized.common.util.category.NutrientCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PropertyValue {
    private static final NutrientCategory[] CATEGORY = NutrientCategory.values();
    private final float[] value = new float[CATEGORY.length];
    private int size;

    public static final Codec<NutrientCategory> CATEGORY_CODEC = StringRepresentable.fromEnum(NutrientCategory::values);
    public static final Codec<PropertyValue> CODEC = Codec.unboundedMap(CATEGORY_CODEC, Codec.FLOAT).xmap(
            PropertyValue::putToMap, propertyValues ->
                    propertyValues.toSet().stream().collect(Collectors.toUnmodifiableMap(Pair::getKey, Pair::getValue))
    );;
    public static final StreamCodec<FriendlyByteBuf, PropertyValue> STREAM_CODEC = StreamCodec.of(
            PropertyValue::toNetwork, PropertyValue::fromNetwork
    );

    public PropertyValue() {
    }

    public static PropertyValue putToMap(@NotNull Map<NutrientCategory, Float> map) {
        PropertyValue propertyValue = new PropertyValue();
        map.forEach(propertyValue::put);
        return propertyValue;
    }

    public boolean has(@NotNull NutrientCategory category) {
        return value[category.ordinal()] > 0.0F;
    }
    public boolean notHas(@NotNull NutrientCategory category) {
        return !this.has(category);
    }

    public void put(@NotNull NutrientCategory category, float value) {
        if (Float.isNaN(value) || value <= 0.0F) {
            this.remove(category);
            return;
        }
        if (notHas(category)) {
            size++;
        }
        this.value[category.ordinal()] = value;
    }

    public void remove(@NotNull NutrientCategory category) {
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

    private static PropertyValue fromNetwork(FriendlyByteBuf buffer) {
        PropertyValue propertyValue = new PropertyValue();
        int length = buffer.readByte();
        IntStream.range(0, length).forEach(i -> {
            NutrientCategory category = buffer.readEnum(NutrientCategory.class);
            float value = buffer.readFloat();
            propertyValue.put(category, value);
        });
        return propertyValue;
    }

    private static void toNetwork(FriendlyByteBuf buffer, PropertyValue value) {
        Set<Pair<NutrientCategory, Float>> entrySet = value.toSet();
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

    public float getBigger(@NotNull NutrientCategory category) {
        return Math.max(value[category.ordinal()], 0.0F);
    }

    public static NutrientCategory[] getCategorie() {
        return CATEGORY;
    }

    public float[] getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }
}
