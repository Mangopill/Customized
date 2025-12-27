package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderSimpleAdjustItemStack;
import mangopill.customized.common.block.entity.CrateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;

import static mangopill.customized.client.util.ClientUtil.*;

public class CrateBlockRenderer implements BlockEntityRenderer<CrateBlockEntity> {

    public CrateBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrateBlockEntity blockEntity, float v,  PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, 14680064, overlay,
                new RenderSimpleAdjustItemStack(blockEntity.getTemplateItem(), blockEntity.getBlockState(), 0.5F, 0.5F, 0.95F,
                        0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F));
    }
}
