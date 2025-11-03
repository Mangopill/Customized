package mangopill.customized.client.event.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.CrateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static mangopill.customized.client.util.ClientUtil.*;

public class CrateBlockRenderer implements BlockEntityRenderer<CrateBlockEntity> {
    public CrateBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrateBlockEntity crateBlockEntity, float v,  PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderBasicBlock(crateBlockEntity, poseStack, buffer, 14680064, overlay, 0.5F, 0.5F, 0.95F, 0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F);
    }
}
