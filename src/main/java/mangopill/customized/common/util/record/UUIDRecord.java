package mangopill.customized.common.util.record;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record UUIDRecord(UUID uuid) {
    public static final UUIDRecord EMPTY = new UUIDRecord(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    public static final Codec<UUIDRecord> CODEC = UUIDUtil.CODEC.xmap(UUIDRecord::new, UUIDRecord::uuid);
    public static final StreamCodec<ByteBuf, UUIDRecord> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(UUIDRecord::new, UUIDRecord::uuid);
}
