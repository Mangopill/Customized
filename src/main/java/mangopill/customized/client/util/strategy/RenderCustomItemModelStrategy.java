package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public record RenderCustomItemModelStrategy(ItemStack stack, ItemDisplayContext displayContext, ResourceLocation resourceLocation, boolean leftHand) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(resourceLocation));
        Minecraft.getInstance().getItemRenderer().render(stack, displayContext, leftHand, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }
}
