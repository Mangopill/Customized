package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.SoupBowlBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.client.util.ClientUtil.renderDrivePlate;

public class SoupBowlBlockRenderer implements BlockEntityRenderer<SoupBowlBlockEntity> {

    public SoupBowlBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull SoupBowlBlockEntity soupBowlBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        renderDrivePlate(soupBowlBlockEntity, poseStack, multiBufferSource, light, overlay, 0.295F, 0.295F, 0.25F, 0.695F, 0.695F, 0.3125F);
    }
}
