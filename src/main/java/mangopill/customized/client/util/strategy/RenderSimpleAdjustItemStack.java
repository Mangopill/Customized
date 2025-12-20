package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static mangopill.customized.client.util.ClientUtil.*;

public record RenderSimpleAdjustItemStack(ItemStack itemStack, @Nullable BlockState state,
                                          float startX, float startY, float startZ,
                                          float scaleX, float scaleY, float scaleZ,
                                          float degreesX, float degreesY, float degreesZ) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        adjust(poseStack, state, startX, startY, startZ);
        poseStack.scale(scaleX, scaleY, scaleZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(degreesX));
        poseStack.mulPose(Axis.YP.rotationDegrees(degreesY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(degreesZ));
        if (!itemStack.isEmpty()) {
            ItemStack renderItemStack = itemStack.copyWithCount(1);
            Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
        }
        poseStack.popPose();
    }
}
