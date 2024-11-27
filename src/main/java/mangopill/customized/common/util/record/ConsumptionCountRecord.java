package mangopill.customized.common.util.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ConsumptionCountRecord(int consumptionCount) {
    public static final ConsumptionCountRecord NULL = new ConsumptionCountRecord(0);

    public static final Codec<ConsumptionCountRecord> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("consumption_count").forGetter(ConsumptionCountRecord::consumptionCount)
            ).apply(instance, ConsumptionCountRecord::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ConsumptionCountRecord> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ConsumptionCountRecord::consumptionCount,
            ConsumptionCountRecord::new
    );
}
