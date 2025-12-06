package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;

import static mangopill.customized.client.util.ClientUtil.*;

public class BakingPanBlockRenderer implements BlockEntityRenderer<BakingPanBlockEntity> {
    public BakingPanBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BakingPanBlockEntity bakingPanBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        renderLayeredPlate(bakingPanBlockEntity, poseStack, multiBufferSource, light, overlay, 0.28F, 0.28F, 0.05F, 0.72F, 0.72F, 0.1F);
    }
}
