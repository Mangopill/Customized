package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

public class CasseroleBlockRenderer implements BlockEntityRenderer<AbstractPotBlockEntity> {

    public CasseroleBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AbstractPotBlockEntity casseroleBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        if (casseroleBlockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) {
            return;
        }
        renderDrivePot(casseroleBlockEntity, poseStack, multiBufferSource, light, overlay, 0.22F, 0.22F, 0.3F, 0.78F, 0.78F, 0.4678F, getCLoc("block/casserole_drive"), 0.3F);
    }
}