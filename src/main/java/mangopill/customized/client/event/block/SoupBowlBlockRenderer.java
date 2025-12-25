package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderItemListDriveStrategy;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class SoupBowlBlockRenderer implements BlockEntityRenderer<AbstractPlateBlockEntity> {

    public SoupBowlBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(AbstractPlateBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> stackList = blockEntity.getItemStackListInPlate(false);
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, light, overlay,
                new RenderItemListDriveStrategy(blockEntity.getLevel(), stackList,
                        0.295F, 0.295F, 0.25F, 0.695F, 0.695F, 0.3125F,
                        null, true));
    }
}
