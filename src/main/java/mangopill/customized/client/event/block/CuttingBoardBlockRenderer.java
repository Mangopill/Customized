package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderSimpleAdjustItemStack;
import mangopill.customized.common.block.entity.CuttingBoardBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class CuttingBoardBlockRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    public CuttingBoardBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CuttingBoardBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        float yOffset = 0.37F;
        int totalTimes = blockEntity.getTotalTimes();
        int times = blockEntity.getTimes();
        if (totalTimes > 1) {
            yOffset -= (totalTimes - times) * ((yOffset - 0.1F) / (totalTimes - 1));
        }
        List<ItemStack> stackList = blockEntity.getItemStackListInBlockEntity(false);
        if (stackList.isEmpty()) return;
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, 14680064, overlay,
                new RenderSimpleAdjustItemStack(stackList.getFirst(), blockEntity.getBlockState(), 0.5F, 0.095F, 0.5F,
                        0.4F, yOffset, 0.4F, 90.0F, 0.0F, 0));
    }
}