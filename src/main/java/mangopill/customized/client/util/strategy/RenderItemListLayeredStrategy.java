package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mangopill.customized.client.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.*;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public record RenderItemListLayeredStrategy(List<ItemStack> stackList, float startLength, float startWidth, float startHeight,
                                            float endLength, float endWidth, float endHeight) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (newStack.isEmpty()) continue;
            int count = newStack.getCount();
            int renderCount = count / 4 + (count % 4 > 0 ? 1 : 0);
            for (int i = 0; i < renderCount; i++) {
                ClientUtil.RandomXYZ randomXYZ = getRandomXYZ(stackList, startLength, startWidth, startHeight, endLength, endWidth, endHeight, stack, i, newStack);
                poseStack.pushPose();
                poseStack.translate(randomXYZ.randX(), randomXYZ.randY(), randomXYZ.randZ());
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomXYZ.random().nextFloat(360.0F)));
                poseStack.scale(0.2F, 0.2F, 0.2F);
                ItemStack renderItemStack = newStack.copyWithCount(1);
                Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        }
    }
}
