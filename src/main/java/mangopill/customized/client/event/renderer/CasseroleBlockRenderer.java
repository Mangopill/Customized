package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.entity.CasseroleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.client.util.ClientUtil.*;

public class CasseroleBlockRenderer implements BlockEntityRenderer<CasseroleBlockEntity> {

    public CasseroleBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull CasseroleBlockEntity casseroleBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        renderDrivePot(casseroleBlockEntity, poseStack, multiBufferSource, light, overlay, 0.25F, 0.25F, 0.1F, 0.75F, 0.75F, 0.45F);
    }
}
