package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.CuttingBoardBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static mangopill.customized.client.util.ClientUtil.renderBasicBlock;

public class CuttingBoardBlockRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {
    public CuttingBoardBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CuttingBoardBlockEntity cuttingBoardBlockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        float yOffset = 0.37F;
        int totalTimes = cuttingBoardBlockEntity.getTotalTimes();
        int times = cuttingBoardBlockEntity.getTimes();
        if (totalTimes > 1) {
            yOffset -= (totalTimes - times) * ((yOffset - 0.1F) / (totalTimes - 1));
        }
        renderBasicBlock(cuttingBoardBlockEntity, poseStack, buffer, 14680064, overlay, 0.5F, 0.095F, 0.5F, 0.4F, yOffset, 0.4F, 90.0F, 0.0F, 0.0F);
    }
}