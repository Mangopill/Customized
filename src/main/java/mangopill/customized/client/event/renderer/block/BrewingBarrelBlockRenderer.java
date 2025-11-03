package mangopill.customized.client.event.renderer.block;

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
    public void render(BrewingBarrelBlockEntity barrelBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        renderBrewingBarrel(barrelBlockEntity, poseStack, multiBufferSource, 14680064, overlay, 0.125F, 0.125F, 0.06F, 0.1F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F);
    }
}
