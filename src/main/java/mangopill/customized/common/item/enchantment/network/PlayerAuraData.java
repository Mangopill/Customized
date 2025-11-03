package mangopill.customized.common.item.enchantment.network;

import io.netty.buffer.ByteBuf;
import mangopill.customized.common.item.enchantment.AuraOfCulinaryArtsEnchantmentEffect;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;

import static mangopill.customized.common.util.ResourceUtil.*;

public record PlayerAuraData(
        Map<String, AuraOfCulinaryArtsEnchantmentEffect.CulinaryAuraData> playerAuraData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerAuraData> TYPE = new CustomPacketPayload.Type<>(getCLoc("player_aura_data"));

    public static final StreamCodec<ByteBuf, PlayerAuraData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, AuraOfCulinaryArtsEnchantmentEffect.CulinaryAuraData.STREAM_CODEC),
            PlayerAuraData::playerAuraData,
            PlayerAuraData::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}