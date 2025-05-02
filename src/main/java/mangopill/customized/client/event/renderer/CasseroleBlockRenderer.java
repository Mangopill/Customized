package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.CasseroleBlockEntity;
import mangopill.customized.common.block.state.PotState;
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
        if (casseroleBlockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            return;
        }
        renderDrivePot(casseroleBlockEntity, poseStack, multiBufferSource, light, overlay, 0.1875F, 0.1875F, 0.125F, 0.8125F, 0.8125F, 0.4678F);
    }
}
