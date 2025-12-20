package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.*;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class RoasterBlockRenderer implements BlockEntityRenderer<AbstractPotBlockEntity> {

    public RoasterBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AbstractPotBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> stackList = blockEntity.getItemStackListInPot(false, false);
        renderItemStackByStrategy(poseStack, buffer, light, overlay,
                new RenderItemListLayeredStrategy(stackList, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0.5F));
    }
}