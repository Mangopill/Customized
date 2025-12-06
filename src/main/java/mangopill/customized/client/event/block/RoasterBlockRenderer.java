package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static mangopill.customized.client.util.ClientUtil.*;

public class RoasterBlockRenderer implements BlockEntityRenderer<AbstractPotBlockEntity> {

    public RoasterBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AbstractPotBlockEntity roasterBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        renderLayeredPot(roasterBlockEntity, poseStack, multiBufferSource, light, overlay, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0.5F);
    }
}