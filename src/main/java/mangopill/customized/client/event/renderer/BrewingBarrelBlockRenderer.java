package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.BrewingBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import javax.annotation.Nonnull;

import static mangopill.customized.client.util.ClientUtil.renderBrewingBarrel;

public class BrewingBarrelBlockRenderer implements BlockEntityRenderer<BrewingBarrelBlockEntity> {

    public BrewingBarrelBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@Nonnull BrewingBarrelBlockEntity barrelBlockEntity, float v, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource multiBufferSource, int light, int overlay) {
        renderBrewingBarrel(barrelBlockEntity.getLevel(), barrelBlockEntity, poseStack, multiBufferSource, 14680064, overlay, 0.125F, 0.125F, 0.06F);
    }
}
