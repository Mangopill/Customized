package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderItemListLayeredStrategy;
import mangopill.customized.common.block.entity.BakingPanBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class BakingPanBlockRenderer implements BlockEntityRenderer<BakingPanBlockEntity> {

    public BakingPanBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BakingPanBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> stackList = blockEntity.getItemStackListInPlate(false);
        renderItemStackByStrategy(poseStack, buffer, light, overlay,
                new RenderItemListLayeredStrategy(stackList, 0.28F, 0.28F, 0.05F, 0.72F, 0.72F, 0.1F));
    }
}
