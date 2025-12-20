package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;

public record RenderHatModelStrategy<T extends LivingEntity, M extends EntityModel<T>> (M model, ItemStack stack, float scale, double translateY, ResourceLocation texture) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        if (model instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(poseStack);
        }
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0D, translateY, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, itemRenderer.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.standalone(texture)));
        poseStack.popPose();
    }
}
