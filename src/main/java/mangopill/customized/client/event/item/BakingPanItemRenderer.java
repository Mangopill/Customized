package mangopill.customized.client.event.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.item.AbstractPlateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

public class BakingPanItemRenderer extends BlockEntityWithoutLevelRenderer {

    public BakingPanItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (!(stack.getItem() instanceof AbstractPlateItem plateItem)) return;
        List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
        renderItemStackByStrategy(poseStack, buffer, light, overlay,
                new RenderCustomItemModelStrategy(stack, ItemDisplayContext.NONE, getCLoc("item/baking_pan_with_drive_renderer"), false),
                new RenderItemListLayeredStrategy(stackList, 0.28F, 0.28F, 0.05F, 0.72F, 0.72F, 0.1F));
    }

    public static class BakingPanItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new BakingPanItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
