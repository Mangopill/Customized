package mangopill.customized.common.item.enchantment.network;

import mangopill.customized.client.event.player.AuraOfCulinaryArtsEnchantmentEffectRenderer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleDataOnMain(final PlayerAuraData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            AuraOfCulinaryArtsEnchantmentEffectRenderer.CLIENT_AURA_DATA.clear();
            AuraOfCulinaryArtsEnchantmentEffectRenderer.CLIENT_AURA_DATA.putAll(data.playerAuraData());
        });
    }
}