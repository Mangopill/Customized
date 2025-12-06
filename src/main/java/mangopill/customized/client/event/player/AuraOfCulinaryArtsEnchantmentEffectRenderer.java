package mangopill.customized.client.event.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mangopill.customized.common.item.enchantment.AuraOfCulinaryArtsEnchantmentEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class AuraOfCulinaryArtsEnchantmentEffectRenderer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    public static final Map<String, AuraOfCulinaryArtsEnchantmentEffect.CulinaryAuraData> CLIENT_AURA_DATA = new HashMap<>();
    public AuraOfCulinaryArtsEnchantmentEffectRenderer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack,  MultiBufferSource bufferSource, int packedLight,
                       T player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderAuraOfCulinaryArtsEnchantmentEffect(poseStack, bufferSource, packedLight, player, partialTicks);
    }

    private static <T extends Player> void renderAuraOfCulinaryArtsEnchantmentEffect(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T player, float partialTicks) {
        Level level = player.level();
        AuraOfCulinaryArtsEnchantmentEffect.CulinaryAuraData data = CLIENT_AURA_DATA.get(player.getUUID().toString());
        if (data == null || data.getActiveFoods() <= 0 || data.getStackList().size() < data.getActiveFoods()) {
            return;
        }
        float time = (level.getGameTime() + partialTicks) * 0.05F;
        for (int i = 0; i < data.getActiveFoods(); i++) {
            poseStack.pushPose();
            double angle = time + (i * Math.PI * 2 / data.getActiveFoods());
            double x = Math.cos(angle) * 1.2;
            double z = Math.sin(angle) * 1.2;
            double y = 0;
            poseStack.translate(x, y, z);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 30.0F));
            poseStack.scale(0.4F, 0.4F, 0.4F);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    data.getStackList().get(i),
                    ItemDisplayContext.FIXED,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    level,
                    0
            );
            poseStack.popPose();
        }
    }

}
