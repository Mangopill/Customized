package mangopill.customized.common.util.value;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.*;

import java.util.*;

public class PropertyValue {
    public static final Codec<PropertyValue> CODEC = Codec.unboundedMap(Codec.STRING, Codec.FLOAT).xmap(PropertyValue::new, PropertyValue::getValue);
    public static final StreamCodec<FriendlyByteBuf, PropertyValue> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.FLOAT), PropertyValue::getValue, PropertyValue::new);
    private final Map<String, Float> valueMap = new HashMap<>();

    public PropertyValue() {
    }

    public PropertyValue(Map<String, Float> valueMap) {
        for (Map.Entry<String, Float> entry : valueMap.entrySet()) {
            put(entry.getKey(), entry.getValue());
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

    public Map<String, Float> getValue() {
        return valueMap;
    }
}
