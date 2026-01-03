package mangopill.customized.common.util.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.*;

public record ConsumptionCountTotalRecord(int consumptionCountTotal) {
    public static final ConsumptionCountTotalRecord EMPTY = new ConsumptionCountTotalRecord(0);

    public static final Codec<ConsumptionCountTotalRecord> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("consumption_count_total").forGetter(ConsumptionCountTotalRecord::consumptionCountTotal)
            ).apply(instance, ConsumptionCountTotalRecord::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ConsumptionCountTotalRecord> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ConsumptionCountTotalRecord::consumptionCountTotal,
            ConsumptionCountTotalRecord::new
    );
}
