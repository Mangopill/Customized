package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class SteamerBlockRenderer implements BlockEntityRenderer<AbstractPotBlockEntity> {

    public SteamerBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AbstractPotBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> stackList = blockEntity.getItemStackListInPot(false, false);
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, light, overlay,
                new RenderFluidInBlockStrategy(blockEntity.getLevel(), blockEntity.getBlockPos(),
                        blockEntity.getFluidHandler(), stackList,
                        0.125F, 0.13F, 0.125F, 0.875F, 0.25F, 0.875F, true),
                new RenderItemListLayeredStrategy(stackList,
                        0.1875F, 0.1875F, 0.375F, 0.8125F, 0.8125F, 0.38F));
    }
}