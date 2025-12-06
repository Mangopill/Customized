package mangopill.customized.common.util.value;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public class PropertyValue {
    private final Map<String, Float> valueMap = new HashMap<>();

    public static final Codec<PropertyValue> CODEC = Codec.unboundedMap(Codec.STRING, Codec.FLOAT).xmap(PropertyValue::new, propertyValue -> propertyValue.valueMap);
    public static final StreamCodec<FriendlyByteBuf, PropertyValue> STREAM_CODEC = StreamCodec.of(PropertyValue::toNetwork, PropertyValue::fromNetwork);

    public PropertyValue() {
    }

    public PropertyValue(Map<String, Float> valueMap) {
        for (Map.Entry<String, Float> entry : valueMap.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void put(String category, float value) {
        if (Float.isNaN(value) || value <= 0.0F) {
            valueMap.remove(category);
            return;
        }
        valueMap.put(category, value);
    }

    public boolean isEmpty() {
        return valueMap.isEmpty();
    }

    private static PropertyValue fromNetwork(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        Map<String, Float> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(buffer.readUtf(), buffer.readFloat());
        }
        return new PropertyValue(map);
    }

    private static void toNetwork(FriendlyByteBuf buffer, PropertyValue value) {
        buffer.writeVarInt(value.valueMap.size());
        for (Map.Entry<String, Float> entry : value.valueMap.entrySet()) {
            buffer.writeUtf(entry.getKey());
            buffer.writeFloat(entry.getValue());
        }
    }

    public Map<String, Float> getValue() {
        return valueMap;
    }
}
