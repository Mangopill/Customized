package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.BrewingBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static mangopill.customized.client.util.ClientUtil.*;

public class BrewingBarrelBlockRenderer implements BlockEntityRenderer<BrewingBarrelBlockEntity> {

    public BrewingBarrelBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BrewingBarrelBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderBrewingBarrel(blockEntity, poseStack, buffer, 14680064, overlay, 0.125F, 0.125F, 0.06F, 0.1F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F);
    }
}
