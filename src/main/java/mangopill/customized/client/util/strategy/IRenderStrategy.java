package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

@FunctionalInterface
public interface IRenderStrategy {
    void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay);
}
