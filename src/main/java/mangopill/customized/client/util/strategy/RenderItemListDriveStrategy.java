package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public record RenderItemListDriveStrategy(@Nullable Level level, List<ItemStack> stackList, float startLength, float startWidth, float startHeight,
                                          float endLength, float endWidth, float endHeight, @Nullable RenderType renderType, boolean dynamic) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (level == null) return;
        float globalTime = level.getGameTime() % 24000 * 0.05F;
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (newStack.isEmpty()) continue;
            int count = newStack.getCount();
            int renderCount = count / 4 + (count % 4 > 0 ? 1 : 0);
            for (int i = 0; i < renderCount; i++) {
                RandomXYZ randomXYZ = getRandomXYZ(stackList, startLength, startWidth, startHeight, endLength, endWidth, endHeight, stack, i, newStack);
                float phase = (randomXYZ.seed() % 1000) * 0.1F;
                float deltaY = (float) Math.sin(globalTime * 0.8F + phase) * 0.03F;
                float animatedY = dynamic ? randomXYZ.randY() + deltaY : randomXYZ.randY();
                poseStack.pushPose();
                poseStack.translate(randomXYZ.randX(), animatedY, randomXYZ.randZ());
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomXYZ.random().nextFloat(360.0F)));
                poseStack.mulPose(Axis.XP.rotationDegrees(randomXYZ.random().nextFloat(360.0F)));
                poseStack.mulPose(Axis.YP.rotationDegrees(randomXYZ.random().nextFloat(360.0F)));
                poseStack.scale(0.3F, 0.3F, 0.3F);
                ItemStack renderItemStack = newStack.copyWithCount(1);
                if (renderType != null) {
                    BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(renderItemStack, null, null, 0);
                    model = ClientHooks.handleCameraTransforms(poseStack, model, ItemDisplayContext.FIXED, false);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);
                    Minecraft.getInstance().getItemRenderer().renderModelLists(model, renderItemStack, light, overlay, poseStack, buffer.getBuffer(renderType));
                } else {
                    Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                }
                poseStack.popPose();
            }
        }
    }
}
