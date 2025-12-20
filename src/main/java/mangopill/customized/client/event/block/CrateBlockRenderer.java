package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.block.entity.CrateBlockEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class CrateBlockRenderer implements BlockEntityRenderer<CrateBlockEntity> {

    public CrateBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrateBlockEntity blockEntity, float v,  PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> stackList = blockEntity.getItemStackListInBlockEntity(false);
        if (stackList.isEmpty()) return;
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, 14680064, overlay,
                new RenderSimpleAdjustItemStack(stackList.getFirst(), blockEntity.getBlockState(), 0.5F, 0.5F, 0.95F,
                        0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F));
    }
}
